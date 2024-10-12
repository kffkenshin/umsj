package cn.yessoft.umsj.moduler.xinhefa.quartz;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;
import static cn.yessoft.umsj.moduler.xinhefa.enums.XHFErrorCodeConstants.SO_ITEM_NOT_MATCH;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.common.utils.json.JsonUtils;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDetailDO;
import cn.yessoft.umsj.moduler.base.service.IBaseDictDetailService;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.RejectRateSimulateDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSODetailAPSStatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSODetailT100StatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSyncJobDictConfig;
import cn.yessoft.umsj.moduler.xinhefa.service.*;
import cn.yessoft.umsj.moduler.xinhefa.utils.XHFUtils;
import cn.yessoft.umsj.quartz.core.handler.JobHandler;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Slf4j
public class SyncSaleOrder implements JobHandler {

  @Resource private IBaseDictDetailService baseDictDetailService;

  @Resource private IXhfItemService xhfItemService;

  @Resource private IXhfCustomerItemService xhfCustomerItemService;

  @Resource private IXhfCustomerService xhfCustomerService;

  @Resource private IXhfSaleOrderDetailService xhfSaleOrderDetailService;

  @Resource private IXhfSaleOrderHeaderService xhfSaleOrderHeaderService;

  @Resource private IXhfProductProcessService xhfProductProcessService;

  private static final Integer EACH_QTY = 200;
  private Map<String, Long> proessesMap;

  @Override
  public String execute(String param) throws Exception {
    StringBuilder errMsg = new StringBuilder();
    BaseDictDetailDO lastRunTime =
        baseDictDetailService.getByDictIdAndDetailKey(
            XHFSyncJobDictConfig.SALE_ORDER.getDictId(),
            XHFSyncJobDictConfig.SALE_ORDER.getDetailKey());
    if (lastRunTime == null) {
      throw exception(O_NOT_EXISTS, "销售订单同步定时配置");
    }
    String now = BaseUtils.getCurrentTimeStr();
    // 上次执行的时间
    Map<String, String> lastRun = Maps.newHashMap();
    lastRun.put("lastRun", lastRunTime.getDetailValue());
    List<Entity> result = Db.use().query(SQL, lastRun);
    errMsg.append(handleResult(result));
    // 记录本次时间
    if (!result.isEmpty()) {
      lastRunTime.setDetailValue(now);
      baseDictDetailService.updateById(lastRunTime);
    }
    return (errMsg.isEmpty())
        ? ""
        : ("\n ======错误信息:====== \n" + errMsg) + JsonUtils.toJsonString(result);
  }

  private String handleResult(List<Entity> result) throws SQLException {
    String errMsg = "";
    if (BaseUtils.isNotEmpty(result)) {
      for (Entity i : result) {
        try {
          String orderNo = i.getStr("销售订单号");
          Integer detailSeq = i.getInt("项次");
          XhfSaleOrderHeaderDO header = xhfSaleOrderHeaderService.getByOrderNo(orderNo);
          if (header == null) {
            header = createHeader(i);
            createDetail(header, i);
          } else {
            XhfSaleOrderDetailDO detail =
                xhfSaleOrderDetailService.getBySOIDAndSeq(header.getId(), detailSeq);
            if (detail == null) {
              createDetail(header, i);
            } else {
              updateDetail(detail, i);
            }
            // todo 表头状态变化处理
          }
        } catch (Exception ex) {
          errMsg += ex.getMessage();
        }
      }
    }
    return errMsg;
  }

  private void updateDetail(XhfSaleOrderDetailDO detail, Entity i) {
    String itemNo = i.getStr("料号");
    XhfItemDO item = xhfItemService.getItemByNo(itemNo);
    if (item == null) {
      throw exception(O_NOT_EXISTS, "料号" + itemNo);
    }
    if (!item.getId().equals(detail.getItemId())) {
      throw exception(SO_ITEM_NOT_MATCH, detail.getSoDetailNumber());
    }
    detail.setDeliveredQuantity(i.getBigDecimal("已出货量"));
    detail.setQty(i.getBigDecimal("数量"));
    detail.setPreDeliveryDate(BaseUtils.toLocalDateTime(i.getDate("约定交货日")));
    detail.setRemark(i.getStr("备注"));
    // 印刷米数
    RejectRateSimulateDTO printing = xhfProductProcessService.simulate(itemNo, detail.getQty());
    detail.setPrintQty(
        new BigDecimal(
            printing.getIntro().stream()
                .filter(j -> j.getLabel().equals("投料米数"))
                .findFirst()
                .get()
                .getValue()));
    XHFUtils.ProductUnitConvert units = XHFUtils.getProductUnitConvert(item, detail.getQty());
    detail.setWpcs(units.getWpcs());
    xhfSaleOrderDetailService.save(detail);
  }

  private void createDetail(XhfSaleOrderHeaderDO header, Entity i) {
    if (!XHFSODetailT100StatusEnum.TOBE_APPROVE.getNo().equals(i.getStr("行状态"))) {
      return;
    }
    XhfSaleOrderDetailDO detail = new XhfSaleOrderDetailDO();
    detail.setSoId(header.getId());
    String itemNo = i.getStr("料号");
    XhfItemDO item = xhfItemService.getItemByNo(itemNo);
    if (item == null) {
      throw exception(O_NOT_EXISTS, "料号" + itemNo);
    }
    detail.setItemId(item.getId());
    detail.setT100Status(i.getStr("行状态"));
    detail.setApsStatus(XHFSODetailAPSStatusEnum.TOBE_APPROVE.getNo());
    String customerItemNo = i.getStr("客户料号");
    XhfCustomerItemDO cusItem =
        xhfCustomerItemService.getCustomerItem(customerItemNo, itemNo, i.getStr("客户编码"));
    if (cusItem != null) {
      detail.setCusItemId(cusItem.getItemId());
    }
    detail.setDetailSeq(i.getInt("项次"));
    detail.setAmrpFlag("Y".equals(i.getStr("纳入AMRP计算")));
    detail.setDeliveredQuantity(i.getBigDecimal("已出货量"));
    detail.setQty(i.getBigDecimal("数量"));
    detail.setPreDeliveryDate(BaseUtils.toLocalDateTime(i.getDate("约定交货日")));
    detail.setRemark(i.getStr("备注"));
    detail.setPurchasingNuit(i.getStr("单位"));
    detail.setSoDetailNumber(header.getOrderNo() + "-" + detail.getDetailSeq());
    // 印刷米数
    RejectRateSimulateDTO printing = xhfProductProcessService.simulate(itemNo, detail.getQty());
    detail.setPrintQty(
        new BigDecimal(
            printing.getIntro().stream()
                .filter(j -> j.getLabel().equals("投料米数"))
                .findFirst()
                .get()
                .getValue()));
    // 订单万只
    XHFUtils.ProductUnitConvert units = XHFUtils.getProductUnitConvert(item, detail.getQty());
    detail.setWpcs(units.getWpcs());
    // 净库存
    detail.setNetInventory(BigDecimal.ZERO);
    xhfSaleOrderDetailService.save(detail);
  }

  private XhfSaleOrderHeaderDO createHeader(Entity i) {
    XhfSaleOrderHeaderDO header = new XhfSaleOrderHeaderDO();
    header.setStatus(i.getStr("单据状态"));
    header.setOrderNo(i.getStr("销售订单号"));
    header.setOrderDate(BaseUtils.toLocalDateTime(i.getDate("订单日期")));
    XhfCustomerDO cus = xhfCustomerService.getByNo(i.getStr("客户编码"));
    if (cus == null) {
      throw exception(O_NOT_EXISTS, "客户" + i.getStr("客户编码"));
    }
    header.setCustomerId(cus.getId());
    header.setCusSaleOrderNo(i.getStr("客户订单号"));
    xhfSaleOrderHeaderService.save(header);
    return header;
  }

  // todo  dsdata 需要改成可以配置的 用formatter{0}处理,目前为了main好测试 先不做
  public static final String SQL =
      "SELECT xmdaent 企业编号,xmdasite 营运据点,xmdastus 单据状态,xmdadocno 销售订单号,xmdadocdt 订单日期,"
          + "       xmda004 客户编码,pmaal004 客户简称,xmda005 订单性质,xmda006 多交性质,xmda008 来源单号,"
          + "       xmda033 客户订单号,xmda019 纳入AMRP计算,"
          + "       xmdcseq 项次,xmdc027 客户料号,xmdc001 料号,imaal003 品名,imaal004 规格,xmdc006 单位,"
          + "       xmdc007 数量,xmdc045 行状态,xmddseq1 项序,xmddseq2 分批序,"
          + "       xmdd006 分批数量,xmdd031 已转出通量,xmdd014 已出货量,xmdd015 销退数量,xmdc050 备注,"
          + "       xmdc012 预计交货日,xmdc013 预计签收日,xmdd011 约定交货日,"
          + "       xmdamoddt 最后更改日期"
          + "  FROM DSDATA.xmda_t"
          + "  LEFT JOIN DSDATA.xmdc_t ON xmdcent = xmdaent AND xmdcdocno = xmdadocno"
          + "  LEFT JOIN DSDATA.xmdd_t ON xmddent = xmdcent AND xmdddocno = xmdcdocno AND xmddseq = xmdcseq "
          + "  LEFT JOIN DSDATA.pmaal_t ON pmaalent = xmdaent AND pmaal001 = xmda004 AND pmaal002 = 'zh_CN' "
          + "  LEFT JOIN DSDATA.imaal_t ON imaalent = xmdcent AND imaal001 = xmdc001 AND imaal002 = 'zh_CN' "
          + " WHERE xmdaent = 100"
          + "  AND xmdasite = 'PHXHF'"
          + "  AND (xmdamoddt >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS') "
          + "  or XMDACRTDT >=  TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS') "
          + "  or xmdd023 >=  TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS')) ";

  // 测试用
  public static void main(String[] args) throws SQLException {
    String now = BaseUtils.getCurrentTimeStr();
    Map<String, String> lastrun = Maps.newHashMap();
    lastrun.put("lastRun", "2024-09-24 14:26:40");
    List<Entity> routes = Db.use().query(SQL, lastrun);
    System.out.println(JsonUtils.toJsonString(routes));
  }
}
