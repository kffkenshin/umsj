package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderBatchDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderDetailDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfTobeScheduledDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MoDetailForSchuderDTO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFMODetailStatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSchedulerModeEnum;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfManufactureOrderDetailMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfManufactureOrderDetailService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfTobeScheduledService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    List<MoDetailForSchuderDTO> datas = prepareData(tobe.getWorkStation(), tobe.getMachineNo());
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
      case B -> {}
      case C -> {}
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
    while (unSolid.size() > 0) {
      MoDetailForSchuderDTO d = getOneModeA(unSolid, lastSolid, needYM);
      solid.add(d);
      unSolid.remove(d);
    }
  }

  // 获取最相近的
  private MoDetailForSchuderDTO getOneModeA(
      List<MoDetailForSchuderDTO> unSolid, MoDetailForSchuderDTO lastSolid, Boolean needYM) {
    MoDetailForSchuderDTO result = null;
    // 先找 同品号
    List<MoDetailForSchuderDTO> sameitem = unSolid.stream().filter();
  }

  // 准备排产数据
  private List<MoDetailForSchuderDTO> prepareData(Integer workStation, String machineNo) {
    List<MoDetailForSchuderDTO> datas = Lists.newArrayList();
    return datas;
  }

  @Override
  public List<XhfManufactureOrderDetailDO> getByMachinNoAndStartTime(
      String machineNo, LocalDateTime beginTime) {
    return xhfMoDetailMapper.getByMachinNoAndStartTime(machineNo, beginTime);
  }
}
