package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.xinhefa.enums.XHFErrorCodeConstants.SO_IS_EMPTY;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.mo.MoHeaderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MoHeaderDTO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.RejectRateSimulateDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.*;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfManufactureOrderHeaderMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.*;
import cn.yessoft.umsj.moduler.xinhefa.utils.XHFUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 生产订单 服务实现类
 *
 * @author ethan
 * @since 2024-10-23
 */
@Service("xhfMoHeaderService")
public class XhfManufactureOrderHeaderServiceImpl
    extends ServiceImpl<XhfManufactureOrderHeaderMapper, XhfManufactureOrderHeaderDO>
    implements IXhfManufactureOrderHeaderService {

  @Resource private XhfManufactureOrderHeaderMapper moHeaderMapper;
  @Resource private IXhfManufactureOrderBatchService xhfMoBatchService;
  @Resource private IXhfManufactureOrderDetailService xhfMoDetailService;
  @Resource private IXhfManufactureOrderLogService xhfMoLogService;
  @Resource private IXhfSaleOrderDeliverService xhfSaleOrderDeliverService;
  @Resource private IXhfSaleOrderHeaderService xhfSaleOrderHeaderService;
  @Resource private IXhfSaleOrderDetailService xhfSaleOrderDetailService;
  @Resource private IXhfProductProcessService xhfProductProcessService;
  @Resource private IXhfItemService xhfItemService;

  @Override
  @Transactional
  public void createFromSoDeliver(List<Long> soDeliverIds) {
    Set<Long> ids = Sets.newHashSet(soDeliverIds);
    if (BaseUtils.isEmpty(ids)) {
      throw exception(SO_IS_EMPTY);
    }
    ids.forEach(
        eachDeliverId -> {
          XhfSaleOrderDeliverDO deliver = xhfSaleOrderDeliverService.getById(eachDeliverId);
          XhfSaleOrderHeaderDO soHeader = xhfSaleOrderHeaderService.getById(deliver.getHeaderId());
          XhfSaleOrderDetailDO soDetail = xhfSaleOrderDetailService.getById(deliver.getDetailId());
          if (!XHFSODeliverStatusEnum.TOBE_APPROVE.getNo().equals(deliver.getStatus())) {
            return;
          }
          // 入库需求数量 = 批次数量 - 净库存
          BigDecimal netInventoryQty =
              deliver.getNetInventoryQty() == null ? BigDecimal.ZERO : deliver.getNetInventoryQty();
          // 入库需求时间 = 到货日 - 物流天数 - 入库提前期
          LocalDateTime requiredDate =
              deliver
                  .getDeliverDate()
                  .plusDays(deliver.getLogisticsDays() == null ? 0 : -deliver.getLogisticsDays())
                  .plusDays(
                      deliver.getWarehousingLeadDays() == null
                          ? 0
                          : -deliver.getWarehousingLeadDays());

          XhfManufactureOrderHeaderDO moHeader =
              createMo(
                  soHeader.getCustomerId(),
                  soDetail,
                  deliver.getQty().subtract(netInventoryQty),
                  requiredDate,
                  XHFMOTypeEnum.P1);
          xhfMoLogService.createMoLog(moHeader, "创建", "由发送入库需求创建");
          deliver.setStatus(XHFSODeliverStatusEnum.TOBE_SCHEDULED.getNo());
          xhfSaleOrderDeliverService.updateById(deliver);
        });
  }

  private XhfManufactureOrderHeaderDO createMo(
      Long customerId,
      XhfSaleOrderDetailDO soDetail,
      BigDecimal qty,
      LocalDateTime requiredDate,
      XHFMOTypeEnum moType) {
    XhfItemDO item = xhfItemService.getById(soDetail.getItemId());
    // 工单头
    XhfManufactureOrderHeaderDO moHeader = createHeader(customerId, soDetail, qty, moType);
    // 工单批次
    XhfManufactureOrderBatchDO moBatch =
        xhfMoBatchService.createHeaderBatch(moHeader, requiredDate);
    // 放量
    RejectRateSimulateDTO fl;
    try {
      fl = xhfProductProcessService.simulate(item, qty);
    } catch (Exception e) {
      moHeader.setRemark(e.getMessage());
      moHeader.setStatus(XHFMOHeaderStatusEnum.ERROR1.getNo());
      moHeaderMapper.updateById(moHeader);
      return moHeader;
    }
    // 工单明细
    if (BaseUtils.isEmpty(fl)) {
      moHeader.setRemark("工艺路线为空!");
      moHeader.setStatus(XHFMOHeaderStatusEnum.ERROR1.getNo());
      moHeaderMapper.updateById(moHeader);
      return moHeader;
    }
    Set<Integer> ws = Sets.newLinkedHashSet();
    fl.getDetail()
        .forEach(
            i -> {
              ws.add(i.getWorkStation());
            });
    XhfManufactureOrderDetailDO parentDetail = null;
    Map<Integer, BigDecimal> leadTime = XHFLeadTimeEnum.getLeadTimeSum(ws, item.getItemType1());
    for (Integer w : ws) {
      parentDetail =
          xhfMoDetailService.createDetail(
              w,
              moBatch,
              parentDetail == null ? 0 : parentDetail.getId(),
              leadTime.get(w),
              fl.getDetail());
    }
    moHeader.setAddtionRate(
        new BigDecimal(
            BaseUtils.objToDouble(
                fl.getIntro().stream()
                    .filter(i -> i.getLabel().equals("放量"))
                    .findFirst()
                    .get()
                    .getValue())));
    moHeader.setStatus(XHFMOHeaderStatusEnum.TOBE_PLANED.getNo());
    updateById(moHeader);
    return moHeader;
  }

  private XhfManufactureOrderHeaderDO createHeader(
      Long customerId, XhfSaleOrderDetailDO soDetail, BigDecimal qty, XHFMOTypeEnum moType) {
    XhfManufactureOrderHeaderDO moHeader = new XhfManufactureOrderHeaderDO();
    moHeader.setQty(qty);
    moHeader.setItemId(soDetail.getItemId());
    moHeader.setCustomerId(customerId);
    moHeader.setType(moType.getNo());
    moHeader.setSoDetailNumber(soDetail.getSoDetailNumber());
    this.save(moHeader);
    return moHeader;
  }

  @Override
  public PageResult<MoHeaderDTO> pagedQuery(MoHeaderQueryReqVO reqVO) {
    PageResult<MoHeaderDTO> result = moHeaderMapper.pagedQuery(reqVO);
    List<Long> headerIds = Lists.newArrayList();
    result.getList().forEach(i -> headerIds.add(i.getId()));
    // 取明细
    List<XhfManufactureOrderDetailDO> details = xhfMoDetailService.getByHeaderIds(headerIds);
    // 取批次
    List<XhfManufactureOrderBatchDO> batchs = xhfMoBatchService.getByHeaderIds(headerIds);
    result
        .getList()
        .forEach(
            i -> {
              if (BaseUtils.isNotEmpty(i.getAddtionRate())) {
                i.setAddtionRateStr(
                    i.getAddtionRate().multiply(XHFUtils.B).setScale(2, BigDecimal.ROUND_UP) + "%");
              }
              i.setQtyConvert(
                  XHFUtils.getProductUnitConvert(
                      i.getItemGWeight(), i.getItemLength(), i.getPurchasingNuit(), i.getQty()));
              List<MoHeaderDTO> children = Lists.newArrayList();
              // 该订单头的批次
              List<XhfManufactureOrderBatchDO> ibatchs =
                  batchs.stream()
                      .filter(e -> e.getHeaderId().equals(i.getId()))
                      .collect(Collectors.toList());
              ibatchs.forEach(
                  e -> {
                    MoHeaderDTO moHeader = new MoHeaderDTO();
                    moHeader.setBatchNo(e.getBatchNo());
                    moHeader.setApsStatusStr(XHFMOBatchStatusEnum.valueOf(e.getStatus()).getName());
                    moHeader.setQty(e.getBatchQty());
                    moHeader.setQtyConvert(
                        XHFUtils.getProductUnitConvert(
                            i.getItemGWeight(),
                            i.getItemLength(),
                            i.getPurchasingNuit(),
                            moHeader.getQty()));
                    List<XhfManufactureOrderDetailDO> idetails =
                        details.stream()
                            .filter(ee -> ee.getBatchId().equals(e.getId()))
                            .collect(Collectors.toList());
                    moHeader.setPrintInputQty(idetails.get(0).getInputQty());
                    moHeader.setRequiedFinishDate(e.getRequireDate());
                    // 投产时间和周别 start
                    moHeader.setRequiedStartDate(
                        e.getRequireDate().plusDays(-idetails.get(0).getLeadTime().longValue()));
                    moHeader.setWeek(LocalDateTimeUtil.weekOfYear(moHeader.getRequiedStartDate()));
                    if (e.getBatchNo() == 1) {
                      setWSStartDates(i, idetails);
                      i.setRequiedFinishDate(e.getRequireDate());
                      i.setRequiedStartDate(moHeader.getRequiedStartDate());
                      i.setWeek(LocalDateTimeUtil.weekOfYear(i.getRequiedStartDate()));
                    }
                    // 投产时间和周别  end
                    i.setPrintInputQty(
                        i.getPrintInputQty() == null
                            ? moHeader.getPrintInputQty()
                            : i.getPrintInputQty().add(moHeader.getPrintInputQty()));
                    setWSStartDates(moHeader, idetails);
                    children.add(moHeader);
                  });
              i.setChildren(children);
            });
    return result;
  }

  private void setWSStartDates(MoHeaderDTO moHeader, List<XhfManufactureOrderDetailDO> idetails) {
    moHeader.setYsStartDateStr(
        idetails.stream()
            .filter(ee -> ee.getWorkStation().equals(XHFWorkStationEnum.YS.getCode()))
            .map(m -> m.getStartTime() == null ? "/" : DateUtil.format(m.getStartTime(), "MM-dd"))
            .findAny()
            .orElse("/"));
    moHeader.setPmStartDateStr(
        idetails.stream()
            .filter(ee -> ee.getWorkStation().equals(XHFWorkStationEnum.PM.getCode()))
            .map(m -> m.getStartTime() == null ? "/" : DateUtil.format(m.getStartTime(), "MM-dd"))
            .findAny()
            .orElse("/"));
    moHeader.setFhStartDateStr(
        idetails.stream()
            .filter(ee -> ee.getWorkStation().equals(XHFWorkStationEnum.FH.getCode()))
            .map(m -> m.getStartTime() == null ? "/" : DateUtil.format(m.getStartTime(), "MM-dd"))
            .findAny()
            .orElse("/"));
    moHeader.setJmStartDateStr(
        idetails.stream()
            .filter(ee -> ee.getWorkStation().equals(XHFWorkStationEnum.JM.getCode()))
            .map(m -> m.getStartTime() == null ? "/" : DateUtil.format(m.getStartTime(), "MM-dd"))
            .findAny()
            .orElse("/"));
    moHeader.setTjStartDateStr(
        idetails.stream()
            .filter(ee -> ee.getWorkStation().equals(XHFWorkStationEnum.TJ.getCode()))
            .map(m -> m.getStartTime() == null ? "/" : DateUtil.format(m.getStartTime(), "MM-dd"))
            .findAny()
            .orElse("/"));
    moHeader.setKmStartDateStr(
        idetails.stream()
            .filter(ee -> ee.getWorkStation().equals(XHFWorkStationEnum.KM.getCode()))
            .map(m -> m.getStartTime() == null ? "/" : DateUtil.format(m.getStartTime(), "MM-dd"))
            .findAny()
            .orElse("/"));
    moHeader.setZxStartDateStr(
        idetails.stream()
            .filter(ee -> ee.getWorkStation().equals(XHFWorkStationEnum.ZDX.getCode()))
            .map(m -> m.getStartTime() == null ? "/" : DateUtil.format(m.getStartTime(), "MM-dd"))
            .findAny()
            .orElse("/"));
    moHeader.setZjStartDateStr(
        idetails.stream()
            .filter(ee -> ee.getWorkStation().equals(XHFWorkStationEnum.ZDJ.getCode()))
            .map(m -> m.getStartTime() == null ? "/" : DateUtil.format(m.getStartTime(), "MM-dd"))
            .findAny()
            .orElse("/"));
  }
}
