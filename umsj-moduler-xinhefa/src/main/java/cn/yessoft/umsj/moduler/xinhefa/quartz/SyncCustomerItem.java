package cn.yessoft.umsj.moduler.xinhefa.quartz;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.common.utils.json.JsonUtils;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDetailDO;
import cn.yessoft.umsj.moduler.base.service.IBaseDictDetailService;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSyncJobDictConfig;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerItemService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemService;
import cn.yessoft.umsj.quartz.core.handler.JobHandler;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.utils.Lists;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class SyncCustomerItem implements JobHandler {

  @Resource private IBaseDictDetailService baseDictDetailService;

  @Resource private IXhfCustomerItemService xhfCustomerItemService;

  @Resource private IXhfItemService xhfItemService;

  @Resource private IXhfCustomerService xhfCustomerService;

  @Override
  public String execute(String param) throws Exception {
    StringBuilder errMsg = new StringBuilder();
    BaseDictDetailDO lastRunTime =
        baseDictDetailService.getByDictIdAndDetailKey(
            XHFSyncJobDictConfig.CUSTOMER_ITEM_INFO.getDictId(),
            XHFSyncJobDictConfig.CUSTOMER_ITEM_INFO.getDetailKey());
    if (lastRunTime == null) {
      throw exception(O_NOT_EXISTS, "客户物料同步定时配置");
    }
    String now = BaseUtils.getCurrentTimeStr();
    // 上次执行的时间
    Map<String, String> lastRun = Maps.newHashMap();
    lastRun.put("lastRun", lastRunTime.getDetailValue());
    List<Entity> result = Lists.newArrayList();
    if ("INIT".equals(lastRunTime.getDetailValue())) {
      List<Entity> count = Db.use().query(INIT_SQL_COUNT);
      Long counts = count.get(0).getLong("counts");
      for (long e = 1L; e * 5000 <= counts; e++) {
        result = Db.use().query(INIT_SQL, 5000 * e);
        errMsg.append(handleResult(result));
      }
    } else {
      result = Db.use().query(SQL, lastRun);
      errMsg.append(handleResult(result));
    }
    if (!result.isEmpty()) {
      // 记录本次时间
      lastRunTime.setDetailValue(now);
      baseDictDetailService.updateById(lastRunTime);
    }
    return (errMsg.isEmpty())
        ? ""
        : ("\n ======错误信息:====== \n" + errMsg) + JsonUtils.toJsonString(result);
  }

  private String handleResult(List<Entity> result) {
    StringBuilder errMsg = new StringBuilder();
    List<XhfCustomerItemDO> needInsert = Lists.newArrayList();
    if (BaseUtils.isNotEmpty(result)) {
      int count = 0;
      for (Entity i : result) {
        XhfCustomerItemDO r =
            xhfCustomerItemService.getCustomerItem(
                i.getStr("客户料号"), i.getStr("内部料号"), i.getStr("客户编号"));
        if (r != null) {
          String em = fillData(r, i);
          if (em.isEmpty()) {
            xhfCustomerItemService.updateById(r);
          } else {
            errMsg.append(em);
          }
        } else {
          r = new XhfCustomerItemDO();
          String em = fillData(r, i);
          if (em.isEmpty()) {
            needInsert.add(r);
          } else {
            errMsg.append(em);
          }
        }
        System.out.println("==================" + count++);
      }
      if (!needInsert.isEmpty()) {
        xhfCustomerItemService.insertBatch(needInsert);
      }
    }
    return errMsg.toString();
  }

  private String fillData(XhfCustomerItemDO item, Entity i) {
    XhfItemDO pitem = xhfItemService.getItemByNo(i.getStr("内部料号"));
    if (pitem == null) {
      return "料号为空:" + i.getStr("内部料号") + "\n";
    }
    XhfCustomerDO cus = xhfCustomerService.getByNo(i.getStr("客户编号"));
    if (cus == null) {
      return "客户为空:" + i.getStr("客户编号") + "\n";
    }
    item.setCustomerItemNo(i.getStr("客户料号"));
    item.setItemId(pitem.getId());
    item.setCustomerItemSpec(i.getStr("客户规格"));
    item.setCustomerItemName(i.getStr("客户品名"));
    item.setCusomerId(cus.getId());
    return "";
  }

  // todo  dsdata 需要改成可以配置的 用formatter{0}处理,目前为了main好测试 先不做
  private static final String SQL =
      "SELECT pmaoent 企业编号,pmaostus 资料状态,pmao000 类别,pmaa090 客户分类编码,oocql004 客户分类名称,"
          + "       pmao001 客户编号,pmaal004 客户简称,pmao002 内部料号,pmao004 客户料号,pmao009 客户品名,pmao010 客户规格,"
          + "       pmaomoddt 最近修改日期"
          + "  FROM DSDATA.pmao_t"
          + "  LEFT JOIN DSDATA.pmaa_t ON pmaaent = pmaoent AND pmaa001 = pmao001"
          + "  LEFT JOIN DSDATA.pmaal_t ON pmaalent = pmaoent AND pmaal001 = pmao001 AND pmaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.oocql_t ON oocqlent = pmaaent AND oocql001 = '281' AND oocql002 = pmaa090 AND oocql003 = 'zh_CN'"
          + "  WHERE pmaoent = 100 "
          + "        AND pmao000 = 2"
          + "        AND (pmaomoddt >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS') "
          + "          or PMAOCRTDT >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS') "
          + "          or PMAAMODDT >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS') "
          + "          or PMAACRTDT >= TO_DATE(:lastRun, 'YYYY-MM-DD HH24:MI:SS') "
          + "        )";

  private static final String INIT_SQL =
      "SELECT pmaoent 企业编号,pmaostus 资料状态,pmao000 类别,pmaa090 客户分类编码,oocql004 客户分类名称,"
          + "       pmao001 客户编号,pmaal004 客户简称,pmao002 内部料号,pmao004 客户料号,pmao009 客户品名,pmao010 客户规格,"
          + "       pmaomoddt 最近修改日期"
          + "  FROM DSDATA.pmao_t"
          + "  LEFT JOIN DSDATA.pmaa_t ON pmaaent = pmaoent AND pmaa001 = pmao001"
          + "  LEFT JOIN DSDATA.pmaal_t ON pmaalent = pmaoent AND pmaal001 = pmao001 AND pmaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.oocql_t ON oocqlent = pmaaent AND oocql001 = '281' AND oocql002 = pmaa090 AND oocql003 = 'zh_CN'"
          + " WHERE pmaoent = 100 "
          + "   AND pmao000 = 2"
          + " OFFSET ? ROWS FETCH NEXT 5000 ROWS ONLY";

  private static final String INIT_SQL_COUNT =
      "SELECT count(1) as counts"
          + "  FROM DSDATA.pmao_t"
          + "  LEFT JOIN DSDATA.pmaa_t ON pmaaent = pmaoent AND pmaa001 = pmao001"
          + "  LEFT JOIN DSDATA.pmaal_t ON pmaalent = pmaoent AND pmaal001 = pmao001 AND pmaal002 = 'zh_CN'"
          + "  LEFT JOIN DSDATA.oocql_t ON oocqlent = pmaaent AND oocql001 = '281' AND oocql002 = pmaa090 AND oocql003 = 'zh_CN'"
          + " WHERE pmaoent = 100 "
          + "   AND pmao000 = 2 ";

  // 测试用
  public static void main(String[] args) throws SQLException {
    String now = BaseUtils.getCurrentTimeStr();
    Map<String, String> lastrun = Maps.newHashMap();
    lastrun.put("lastRun", "2024-09-24 14:26:40");
    List<Entity> result = Db.use().query(SQL, lastrun);
    System.out.println(JsonUtils.toJsonString(result));
  }
}
