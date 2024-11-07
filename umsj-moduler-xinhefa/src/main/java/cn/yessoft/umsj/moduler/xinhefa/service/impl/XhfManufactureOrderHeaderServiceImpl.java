package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.xinhefa.enums.XHFErrorCodeConstants.MO_MACHINE_IS_EMPTY;
import static cn.yessoft.umsj.moduler.xinhefa.enums.XHFErrorCodeConstants.SO_IS_EMPTY;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.mo.MoHeaderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.*;
import cn.yessoft.umsj.moduler.xinhefa.enums.*;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfManufactureOrderHeaderMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.*;
import cn.yessoft.umsj.moduler.xinhefa.utils.XHFUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Iterator;
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
  @Resource private IXhfMachinePropertyService xhfMachinePropertyService;
  @Resource private IXhfMachineDisablePlanService xhfMachineDisablePlanService;

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
    moHeader.setStatus(XHFMOHeaderStatusEnum.TOBE_PLANED.getNo());
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

  @Override
  @Transactional
  public String initMo() {
    StringBuilder resultMsg = new StringBuilder();
    List<XhfManufactureOrderHeaderDO> headers =
        getHeadersByStatus(XHFMOHeaderStatusEnum.TOBE_PLANED.getNo());
    List<Long> headerIds = Lists.newArrayList();
    headers.forEach(i -> headerIds.add(i.getId()));
    List<XhfManufactureOrderDetailDO> details = xhfMoDetailService.getByHeaderIds(headerIds);
    List<XhfManufactureOrderBatchDO> batchs = xhfMoBatchService.getByHeaderIds(headerIds);
    Map<String, BigDecimal> machinePool = Maps.newHashMap(); // 可用机台的池子
    headers.forEach(
        i -> {
          MoDTO moDTO = fillData(i, batchs, details);
          try {
            initMachine(moDTO, machinePool);
            sendT100Mo(moDTO);
          } catch (Exception ex) {
            moDTO.getHeader().setStatus(XHFMOHeaderStatusEnum.ERROR1.getNo());
            moDTO.setMsg(ex.getMessage());
          }
          resultMsg.append(moDTO.getMsg());
          this.update(moDTO);
        });
    return resultMsg.toString();
  }

  private void update(MoDTO moDTO) {
    this.updateById(moDTO.getHeader());
    moDTO.getDetails().forEach(i -> xhfMoDetailService.updateById(i));
    moDTO.getBatchs().forEach(i -> xhfMoBatchService.updateById(i));
    xhfMoLogService.createMoLog(moDTO.getHeader(), "初始化工单", moDTO.getMsg());
  }

  private void sendT100Mo(MoDTO mo) {
    StringBuilder resultMsg = new StringBuilder();
  }

  // 选机
  private void initMachine(MoDTO mo, Map<String, BigDecimal> machinePool) {
    Map<Integer, ProductMachinesDTO> machins =
        xhfMachinePropertyService.getMachins(mo.getHeader().getItemId());
    List<XhfManufactureOrderDetailDO> addtion = Lists.newArrayList(); // 存拆出来多台的 多出来的订单detail
    mo.getDetails()
        .forEach(
            i -> {
              ProductMachinesDTO availbelMachines = machins.get(i.getWorkStation());
              XhfItemDO item = xhfItemService.getById(mo.getHeader().getItemId());
              if (availbelMachines == null) {
                throw exception(
                    MO_MACHINE_IS_EMPTY, XHFWorkStationEnum.valueOf(i.getWorkStation()).getName());
              }
              // 卷数
              BigDecimal rollCount =
                  i.getInputQty()
                      .divide(XHFUtils.K)
                      .divide(availbelMachines.getRollLength())
                      .setScale(0, RoundingMode.UP);
              // 已经选好的机器Map
              Map<String, BigDecimal> choosedMachine = Maps.newTreeMap();
              if (availbelMachines.getDefaultNumber() == 1) { // 默认单机的
                chooseOne(choosedMachine, machinePool, availbelMachines, rollCount, item);
                fillSpeedParams(
                    i,
                    availbelMachines.getMachineParamsDTO(
                        choosedMachine.keySet().iterator().next()));
              } else { // 可选是多台机器的情况
                // 一次拿两卷出来选
                while (rollCount.compareTo(BigDecimal.ZERO) > 0) {
                  if (rollCount.compareTo(new BigDecimal(2)) <= 0) {
                    chooseOne(
                        choosedMachine, machinePool, availbelMachines, new BigDecimal(2), item);
                  } else {
                    chooseOne(choosedMachine, machinePool, availbelMachines, rollCount, item);
                  }
                  rollCount = rollCount.subtract(new BigDecimal(2));
                }
                // 依据选机结果拆分台数
                if (choosedMachine.size() == 1) {
                  fillSpeedParams(
                      i,
                      availbelMachines.getMachineParamsDTO(
                          choosedMachine.keySet().iterator().next()));
                } else {
                  BigDecimal leftQty = i.getInputQty().add(BigDecimal.ZERO);
                  BigDecimal orgOutQty = i.getOutputQty().add(BigDecimal.ZERO);
                  BigDecimal rate = orgOutQty.divide(leftQty);
                  Iterator<String> ite =
                      choosedMachine.keySet().iterator(); // 使用Iterator迭代器 获取HashMap中的键集合 拆台数
                  while (ite.hasNext()) {
                    String k = ite.next();
                    BigDecimal v = choosedMachine.get(k);
                    MachineParamsDTO para = availbelMachines.getMachineParamsDTO(k);
                    XhfManufactureOrderDetailDO thisone = i;
                    if (BaseUtils.isNotEmpty(i.getMachineNumber())) {
                      thisone = BeanUtils.toBean(i, XhfManufactureOrderDetailDO.class);
                      addtion.add(thisone);
                    }
                    BigDecimal input =
                        leftQty.compareTo(para.getRollLength().multiply(v)) > 0
                            ? para.getRollLength().multiply(v)
                            : leftQty;
                    thisone.setInputQty(input);
                    thisone.setOutputQty(i.getInputQty().multiply(rate));
                    leftQty = leftQty.subtract(input);
                    fillSpeedParams(thisone, para);
                  }
                }
              }
            });
    mo.getDetails().addAll(addtion); // 拆出来的加进去
  }

  private void chooseOne(
      Map<String, BigDecimal> choosedMachine,
      Map<String, BigDecimal> machinePool,
      ProductMachinesDTO availbelMachines,
      BigDecimal rollCount,
      XhfItemDO item) {

    BigDecimal prodtctTime = new BigDecimal(2400); // 目前最少的时间
    String tempMachine = "";
    if (choosedMachine.size() < availbelMachines.getDefaultNumber()) { // 还没选满机器 从大池子里面找最空的
      BigDecimal e;
      if (availbelMachines.hasFirst()) {
        prodtctTime =
            getTime(machinePool, availbelMachines.getFirstMachine().getMachineNo(), false);
        tempMachine = availbelMachines.getFirstMachine().getMachineNo();
      }
      for (MachineParamsDTO mdto : availbelMachines.getSecondMachines()) {
        e = getTime(machinePool, mdto.getMachineNo(), true);
        if (e.compareTo(prodtctTime) < 0) {
          prodtctTime = e;
          tempMachine = mdto.getMachineNo();
        }
      }
      BigDecimal rollc =
          choosedMachine.get(tempMachine) == null
              ? rollCount
              : choosedMachine.get(tempMachine).add(rollCount);
    } else { // 已经选满了 从已选的里面找最空的
      Iterator<String> ite = choosedMachine.keySet().iterator();
      while (ite.hasNext()) {
        String k = ite.next();
        BigDecimal v = choosedMachine.get(k);
        BigDecimal e = getTime(machinePool, k, false);
        if (e.compareTo(prodtctTime) < 0) {
          prodtctTime = e;
          tempMachine = k;
        }
      }
    }
    BigDecimal rollc =
        choosedMachine.get(tempMachine) == null
            ? rollCount
            : choosedMachine.get(tempMachine).add(rollCount);
    choosedMachine.put(tempMachine, rollc);
    // 计算生产时间
    machinePool.put(
        tempMachine,
        machinePool
            .get(tempMachine)
            .add(
                XHFUtils.caculateProductTime(
                    rollCount.multiply(availbelMachines.getRollLength()),
                    availbelMachines.getMachineParamsDTO(tempMachine).getSpeed(),
                    availbelMachines.getMachineParamsDTO(tempMachine).getSpeedUnit(),
                    availbelMachines.getMachineParamsDTO(tempMachine).getEfficiency(),
                    item)));
  }

  // 从选机池子获取 该机台的已安排时间(小时为单位) 没有的话就去获取后放入
  // needAdd 说明是次选 需要加一点时间  暂定240 十天
  private BigDecimal getTime(
      Map<String, BigDecimal> machinePool, String machineNo, Boolean needAdd) {
    if (machinePool.get(machineNo) != null) {
      return !needAdd
          ? machinePool.get(machineNo)
          : machinePool.get(machineNo).add(new BigDecimal(240));
    } else {
      // 获取当前周 开始的所有工单
      LocalDateTime beginOfThisWeek = XHFUtils.getWeekBegin();
      List<XhfManufactureOrderDetailDO> details =
          xhfMoDetailService.getByMachinNoAndStartTime(machineNo, beginOfThisWeek);
      BigDecimal r = new BigDecimal(0);
      for (XhfManufactureOrderDetailDO detail : details) {
        LocalDateTimeUtil.between(detail.getStartTime(), detail.getEndTime()).getSeconds();
        r =
            r.add(
                new BigDecimal(
                        LocalDateTimeUtil.between(detail.getStartTime(), detail.getEndTime())
                            .getSeconds())
                    .divide(new BigDecimal(3600)));
      }
      // 获取当前周 开始的 二个月内的停机计划
      List<XhfMachineDisablePlanDO> plans =
          xhfMachineDisablePlanService.getByMachineNo(
              machineNo, beginOfThisWeek, beginOfThisWeek.plusDays(60));
      if (BaseUtils.isNotEmpty(plans)) {
        for (XhfMachineDisablePlanDO plan : plans) {
          r =
              r.add(
                  new BigDecimal(
                          LocalDateTimeUtil.between(plan.getStartTime(), plan.getEndTime())
                              .getSeconds())
                      .divide(new BigDecimal(3600)));
        }
      }
      machinePool.put(machineNo, r);
      return r;
    }
  }

  private void fillSpeedParams(XhfManufactureOrderDetailDO i, MachineParamsDTO machine) {
    i.setMachineNumber(machine.getMachineNo());
    i.setSpeed(machine.getSpeed());
    i.setSpeedUnit(machine.getSpeedUnit());
    i.setSpeedEfficiency(machine.getEfficiency());
  }

  @Override
  public List<XhfManufactureOrderHeaderDO> getHeadersByStatus(String statusNo) {
    return moHeaderMapper.getHeadersByStatus(statusNo);
  }

  private MoDTO fillData(
      XhfManufactureOrderHeaderDO header,
      List<XhfManufactureOrderBatchDO> batchs,
      List<XhfManufactureOrderDetailDO> details) {
    MoDTO moDTO = new MoDTO();
    moDTO.setHeader(header);
    moDTO.setBatchs(
        batchs.stream()
            .filter(i -> i.getHeaderId().equals(header.getId()))
            .collect(Collectors.toList()));
    moDTO.setDetails(
        details.stream()
            .filter(i -> i.getHeaderId().equals(header.getId()))
            .collect(Collectors.toList()));
    return moDTO;
  }
}
