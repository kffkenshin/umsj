package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MoDetailForSchuderDTO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFMODetailStatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSchedulerModeEnum;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfManufactureOrderDetailMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.*;
import cn.yessoft.umsj.moduler.xinhefa.utils.XHFUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

/**
 * 生产订单明细 服务实现类
 *
 * @author ethan
 * @since 2024-10-23
 */
@Service("xhfMoDetailService")
public class XhfManufactureOrderDetailServiceImpl
    extends ServiceImpl<XhfManufactureOrderDetailMapper, XhfManufactureOrderDetailDO>
    implements IXhfManufactureOrderDetailService {

  @Resource private XhfManufactureOrderDetailMapper xhfMoDetailMapper;
  @Resource private IXhfTobeScheduledService xhfTobeScheduledService;
  @Resource private IXhfItemService xhfItemService;
  @Resource private IXhfManufactureOrderHeaderService xhfMoHeaderService;
  @Resource private IXhfManufactureOrderBatchService xhfMoBatchService;
  @Resource private IXhfMachinePropertyService xhfMachinePropertyService;
  @Resource private IXhfMachineDisablePlanService xhfMachineDisablePlanService;

  @Override
  public XhfManufactureOrderDetailDO createDetail(
      Integer workStation,
      XhfManufactureOrderBatchDO moBatch,
      long parentDetailId,
      BigDecimal leadTime,
      List<SimulateDetailDTO> detail) {
    XhfManufactureOrderDetailDO detailDO = new XhfManufactureOrderDetailDO();
    detailDO.setBatchId(moBatch.getId());
    detailDO.setWorkStation(workStation);
    detailDO.setHeaderId(moBatch.getHeaderId());
    detailDO.setLeadTime(leadTime);
    // 准备米数
    BigDecimal prepareQty2 = new BigDecimal(0);
    // 过程废品
    BigDecimal processRejectRate = new BigDecimal(0);
    // 异常废品
    BigDecimal actAbnormalRejectRate = new BigDecimal(0);
    // 投入米数
    BigDecimal inputQty2 = new BigDecimal(0);
    // 产出米数
    BigDecimal outputQty = new BigDecimal(0);
    for (SimulateDetailDTO i : detail) {
      if (i.getWorkStation().equals(workStation)) {
        prepareQty2 = prepareQty2.add(i.getPrepareQty2());
        processRejectRate = processRejectRate.add(i.getProcessRejectRate());
        actAbnormalRejectRate = actAbnormalRejectRate.add(i.getActAbnormalRejectRate());
        if (actAbnormalRejectRate.compareTo(new BigDecimal(0)) == 0) {
          inputQty2 = inputQty2.add(i.getInputQty2());
        }
        outputQty = i.getOutputQty();
      }
    }
    detailDO.setInputQty(inputQty2);
    detailDO.setPrepareRejectQty(prepareQty2);
    detailDO.setProcessRejectRate(processRejectRate);
    detailDO.setAbnormalRejectRate(actAbnormalRejectRate);
    detailDO.setOutputQty(outputQty);
    detailDO.setParentDetailId(parentDetailId);
    // 开始时间 默认需求开始时间那周的最后一天
    LocalDateTime start =
        moBatch
            .getRequireDate()
            .plusHours(detailDO.getLeadTime().multiply(XHFUtils.DAYHOUR).longValue());
    if (start.isBefore(LocalDateTime.now())) {
      start = LocalDateTime.now().plusDays(1);
    }
    detailDO.setStartTime(start);
    xhfMoDetailMapper.insert(detailDO);
    return detailDO;
  }

  @Override
  public List<XhfManufactureOrderDetailDO> getByHeaderIds(List<Long> headerIds) {
    return xhfMoDetailMapper.getByHeaderIds(headerIds);
  }

  @Override
  public String executeScheduler() {
    // 一次只排一台机器
    XhfTobeScheduledDO tobe = xhfTobeScheduledService.getFirst();
    if (tobe == null) {
      return "";
    }
    StringBuilder resultMsg = new StringBuilder();
    List<MoDetailForSchuderDTO> datas = prepareData(tobe.getWorkStation(), tobe.getMachineNo(), 3);
    schedule(datas, tobe, resultMsg);
    return resultMsg.toString();
  }

  // 排程
  private void schedule(
      List<MoDetailForSchuderDTO> datas, XhfTobeScheduledDO mode, StringBuilder resultMsg) {
    switch (XHFSchedulerModeEnum.valueof(mode.getMode())) {
      case A1 -> {
        scheduleWithModeA(datas, true, resultMsg);
      }
      case A2 -> {
        scheduleWithModeA(datas, false, resultMsg);
      }
      case B -> {
        scheduleWithModeB(datas, resultMsg);
      }
      case C -> {
        scheduleWithModeC(datas, resultMsg);
      }
    }
  }

  // 印刷效率优先方式
  private void scheduleWithModeA(
      List<MoDetailForSchuderDTO> datas, Boolean needYM, StringBuilder resultMsg) {
    List<MoDetailForSchuderDTO> solid = Lists.newArrayList(); // 已经定序的
    List<MoDetailForSchuderDTO> unSolid = new ArrayList<>(datas); // 还没排的
    // 已完成的和锁排的放进去
    for (MoDetailForSchuderDTO d : datas) {
      if (d.getMoDetail().getStatus() <= XHFMODetailStatusEnum.LOCKED.getNo()) {
        solid.add(d);
        unSolid.remove(d);
      }
    }
    MoDetailForSchuderDTO lastSolid = null;
    if (solid.size() > 1) {
      lastSolid = solid.get(unSolid.size() - 1);
    }
    while (!unSolid.isEmpty()) {
      MoDetailForSchuderDTO d = getOneModeA(unSolid, lastSolid, needYM);
      solid.add(d);
      unSolid.remove(d);
    }
    List<MoDetailForSchuderDTO> space = calculateTime(solid, true);
    if (BaseUtils.isNotEmpty(space)) {
      rePlanEmpty(solid, space, true);
      calculateTime(solid, false);
    }
    datas = solid;
  }

  // 需求日期优先方式
  private void scheduleWithModeB(List<MoDetailForSchuderDTO> datas, StringBuilder resultMsg) {
    List<MoDetailForSchuderDTO> solid = Lists.newArrayList(); // 已经定序的
    List<MoDetailForSchuderDTO> unSolid = new ArrayList<>(datas); // 还没排的
    // 已完成的和锁排的放进去
    for (MoDetailForSchuderDTO d : datas) {
      if (d.getMoDetail().getStatus() <= XHFMODetailStatusEnum.LOCKED.getNo()) {
        solid.add(d);
        unSolid.remove(d);
      }
    }
    while (!unSolid.isEmpty()) {
      MoDetailForSchuderDTO d = getEarliestRequireDate(unSolid);
      solid.add(d);
      unSolid.remove(d);
    }
    List<MoDetailForSchuderDTO> space = calculateTime(solid, true);
    if (BaseUtils.isNotEmpty(space)) {
      rePlanEmpty(solid, space, false);
      calculateTime(solid, false);
    }
    datas = solid;
  }

  // 最早开始时间优先方式
  private void scheduleWithModeC(List<MoDetailForSchuderDTO> datas, StringBuilder resultMsg) {
    List<MoDetailForSchuderDTO> solid = Lists.newArrayList(); // 已经定序的
    List<MoDetailForSchuderDTO> unSolid = new ArrayList<>(datas); // 还没排的
    // 已完成的和锁排的放进去
    for (MoDetailForSchuderDTO d : datas) {
      if (d.getMoDetail().getStatus() <= XHFMODetailStatusEnum.LOCKED.getNo()) {
        solid.add(d);
        unSolid.remove(d);
      }
    }
    while (!unSolid.isEmpty()) {
      MoDetailForSchuderDTO d = getEarliestBegDate(unSolid);
      solid.add(d);
      unSolid.remove(d);
    }
    calculateTime(solid, false);
    datas = solid;
  }

  // 有空闲的往后面放
  private void rePlanEmpty(
      List<MoDetailForSchuderDTO> solid, List<MoDetailForSchuderDTO> space, Boolean isYS) {
    List<MoDetailForSchuderDTO> temp = new ArrayList<>(solid);
    for (MoDetailForSchuderDTO d : space) {
      if (isYS) {
        List<MoDetailForSchuderDTO> samecolor =
            temp.stream()
                .filter(i -> i.getItem().getColorClass().equals(d.getItem().getColorClass()))
                .collect(Collectors.toList());
        if (!samecolor.isEmpty()) {
          insertSuit(solid, samecolor, d);
          continue;
        }
        insertSuit(solid, temp, d);
      }
    }
  }

  private void insertSuit(
      List<MoDetailForSchuderDTO> solid,
      List<MoDetailForSchuderDTO> temp,
      MoDetailForSchuderDTO d) {
    MoDetailForSchuderDTO index = null;
    for (MoDetailForSchuderDTO e : temp) {
      if (d.getMoDetail().getEarliestStartTime().isBefore(e.getMoDetail().getEndTime())) {
        index = e;
        break;
      }
    }
    if (index == null) {
      index = temp.get(temp.size() - 1);
    }
    solid.add(solid.indexOf(index), d);
  }

  // 计算切换 生产时间
  private List<MoDetailForSchuderDTO> calculateTime(
      List<MoDetailForSchuderDTO> solid, Boolean getEmpty) {
    // 开始时间和上一个结束时间之间有空隙的工单
    List<MoDetailForSchuderDTO> empty = Lists.newArrayList();
    MoDetailForSchuderDTO lastSolid = null;
    for (MoDetailForSchuderDTO d : solid) {
      if (lastSolid == null) {
        lastSolid = getLast(d.getMoDetail());
      }
      if (lastSolid.getMoDetail().getEndTime().isAfter(d.getMoDetail().getEarliestStartTime())) {
        if (getEmpty) {
          empty.add(d);
          continue;
        }
        d.getMoDetail().setStartTime(d.getMoDetail().getEarliestStartTime());
      } else {
        d.getMoDetail().setStartTime(lastSolid.getMoDetail().getEndTime().plusSeconds(1));
      }
      d.getMoDetail()
          .setChangeTime(
              XHFUtils.caculateChangeTime(
                  lastSolid.getItem(),
                  d.getItem(),
                  d.getMachineProperty(),
                  d.getMoDetail().getWorkStation(),
                  d.getMoDetail().getInputQty().divide(d.getMachineProperty().getRollLength())));
      d.getMoDetail()
          .setProductionTime(
              XHFUtils.caculateProductTime(
                  d.getMoDetail().getInputQty(),
                  d.getMoDetail().getSpeed(),
                  d.getMoDetail().getSpeedUnit(),
                  d.getMoDetail().getSpeedEfficiency(),
                  d.getItem()));
      d.getMoDetail()
          .setStopTime(XHFUtils.caculateStopTime(d.getMoDetail(), d.getMachineDisablePlans()));
      d.getMoDetail()
          .setEndTime(
              d.getMoDetail()
                  .getStartTime()
                  .plusMinutes(d.getMoDetail().getChangeTime().longValue())
                  .plusMinutes(d.getMoDetail().getProductionTime().longValue())
                  .plusMinutes(d.getMoDetail().getStopTime().longValue()));
      lastSolid = d;
    }
    // 去掉原来数组中的空的
    if (getEmpty && !empty.isEmpty()) {
      empty.stream()
          .forEach(
              i -> {
                solid.remove(i);
              });
    }
    return empty;
  }

  // 获取上一个工单
  private MoDetailForSchuderDTO getLast(XhfManufactureOrderDetailDO moDetail) {
    MoDetailForSchuderDTO result = new MoDetailForSchuderDTO();
    XhfManufactureOrderDetailDO detail = xhfMoDetailMapper.getLastDetail(moDetail);
    if (detail == null) {
      detail = new XhfManufactureOrderDetailDO();
      detail.setEndTime(LocalDateTime.now());
    } else {
      XhfManufactureOrderHeaderDO haeader = xhfMoHeaderService.getById(detail.getHeaderId());
      XhfItemDO item = xhfItemService.getById(haeader.getItemId());
      result.setItem(item);
    }
    result.setMoDetail(detail);
    return result;
  }

  // 获取最相近的
  private MoDetailForSchuderDTO getOneModeA(
      List<MoDetailForSchuderDTO> unSolid, MoDetailForSchuderDTO lastSolid, Boolean needYM) {
    MoDetailForSchuderDTO result = null;
    // 没有上一个 找需求日期最早的
    if (null == lastSolid) {
      return getEarliestRequireDate(unSolid);
    }
    // 先找 同品号 中最早的
    List<MoDetailForSchuderDTO> sameItem =
        unSolid.stream()
            .filter(i -> i.getItem().getId().equals(lastSolid.getItem().getId()))
            .collect(Collectors.toList());
    if (!sameItem.isEmpty()) {
      return getEarliestRequireDate(sameItem);
    }
    // 没有同品号 找同色系
    sameItem =
        unSolid.stream()
            .filter(i -> i.getItem().getColorClass().equals(lastSolid.getItem().getColorClass()))
            .collect(Collectors.toList());
    if (!sameItem.isEmpty()) {
      return getEarliestRequireDate(sameItem);
    }
    // 没有同色系 找同油墨
    if (needYM) {
      sameItem =
          unSolid.stream()
              .filter(i -> i.getItem().getYmtx().equals(lastSolid.getItem().getYmtx()))
              .collect(Collectors.toList());
      if (!sameItem.isEmpty()) {
        return getEarliestRequireDate(sameItem);
      }
    }
    // 没有油墨的 找最早的
    return getEarliestRequireDate(unSolid);
  }

  // 获取需求日最早的
  private MoDetailForSchuderDTO getEarliestRequireDate(List<MoDetailForSchuderDTO> unSolid) {
    return unSolid.stream().min(Comparator.comparing(MoDetailForSchuderDTO::getRequireDate)).get();
  }

  // 获取最早开始时间最早的
  private MoDetailForSchuderDTO getEarliestBegDate(List<MoDetailForSchuderDTO> unSolid) {
    return unSolid.stream().min(Comparator.comparing(MoDetailForSchuderDTO::getStartTime)).get();
  }

  // 准备排产数据
  private List<MoDetailForSchuderDTO> prepareData(
      Integer workStation, String machineNo, Integer weekCount) {
    List<MoDetailForSchuderDTO> datas = Lists.newArrayList();
    // 获取该机台本周开始的,一定时间内的,还没有排的工单
    LocalDateTime start = DateUtil.beginOfWeek(new Date()).toLocalDateTime();
    LocalDateTime end = start.plusWeeks(weekCount);
    List<XhfManufactureOrderDetailDO> details =
        getDetailsToPlan(workStation, machineNo, start, end);
    Set<Long> headerIds = Sets.newHashSet();
    Set<Long> batchIds = Sets.newHashSet();
    Set<Long> machinePropertIds = Sets.newHashSet();
    details.forEach(
        i -> {
          headerIds.add(i.getHeaderId());
          batchIds.add(i.getBatchId());
          machinePropertIds.add(i.getMachineParamsId());
        });
    List<XhfManufactureOrderHeaderDO> headers = xhfMoHeaderService.listByIds(headerIds);
    List<XhfManufactureOrderBatchDO> batches = xhfMoBatchService.listByIds(batchIds);
    Set<Long> itemIds = Sets.newHashSet();
    headers.forEach(i -> itemIds.add(i.getItemId()));
    List<XhfItemDO> items = xhfItemService.listByIds(itemIds);
    List<XhfMachinePropertyDO> machine = xhfMachinePropertyService.listByIds(machinePropertIds);
    List<XhfMachineDisablePlanDO> disablePlans =
        xhfMachineDisablePlanService.getByMachineNo(machineNo, start, end);
    details.forEach(
        i -> {
          MoDetailForSchuderDTO each = new MoDetailForSchuderDTO();
          each.setMoDetail(i);
          each.setMoBatch(
              batches.stream().filter(e -> e.getId().equals(i.getBatchId())).toList().get(0));
          each.setMoHeader(
              headers.stream().filter(e -> e.getId().equals(i.getHeaderId())).toList().get(0));
          each.setMachineProperty(
              machine.stream()
                  .filter(e -> e.getId().equals(i.getMachineParamsId()))
                  .toList()
                  .get(0));
          each.setMachineDisablePlans(disablePlans);
          datas.add(each);
        });
    return datas;
  }

  private List<XhfManufactureOrderDetailDO> getDetailsToPlan(
      Integer workStation, String machineNo, LocalDateTime start, LocalDateTime end) {
    return xhfMoDetailMapper.getDetailsToPlan(workStation, machineNo, start, end);
  }

  @Override
  public List<XhfManufactureOrderDetailDO> getByMachinNoAndStartTime(
      String machineNo, LocalDateTime beginTime) {
    return xhfMoDetailMapper.getByMachinNoAndStartTime(machineNo, beginTime);
  }
}
