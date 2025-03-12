package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.MachineDisablePlanCreateVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDisablePlanDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MachineDisableInfoDTO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfMachineDisablePlanMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachineDisablePlanService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 停机计划 服务实现类
 *
 * @author ethan
 * @since 2024-11-07
 */
@Service("xhfMachineDisablePlanService")
public class XhfMachineDisablePlanServiceImpl
    extends ServiceImpl<XhfMachineDisablePlanMapper, XhfMachineDisablePlanDO>
    implements IXhfMachineDisablePlanService {
  @Resource private XhfMachineDisablePlanMapper xhfMachineDisablePlanMapper;
  @Resource private IXhfMachineService xhfMachineService;

  @Override
  public List<XhfMachineDisablePlanDO> getByMachineNo(
      String machineNo, LocalDateTime beginOfThisWeek, LocalDateTime localDateTime) {
    return xhfMachineDisablePlanMapper.getByMachineNo(machineNo, beginOfThisWeek, localDateTime);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createPlans(MachineDisablePlanCreateVO reqVO) {
    for (Long machineId : reqVO.getMachines()) {
      XhfMachineDO machine = xhfMachineService.getById(machineId);
      if (machine == null) {
        continue;
      }
      String startDate = reqVO.getDateRange().get(0);
      String endDate = reqVO.getDateRange().get(1);
      this.deleteByMachinNoAndRange(machine.getMachineNo(), startDate, endDate);
      LocalDateTime start = LocalDateTimeUtil.parse(startDate, "yyyy-MM-dd");
      LocalDateTime end = LocalDateTimeUtil.parse(endDate, "yyyy-MM-dd");
      while (start.isBefore(end)) {
        if (reqVO.getWeek().contains(LocalDateTimeUtil.dayOfWeek(start.toLocalDate()).getValue())) {
          XhfMachineDisablePlanDO dplan = new XhfMachineDisablePlanDO();
          dplan.setMachineNumber(machine.getMachineNo());
          dplan.setStartTime(
              LocalDateTimeUtil.parse(
                  startDate.concat(" ").concat(reqVO.getStartTime()), "yyyy-MM-dd HH:mm"));
          dplan.setEndTime(
              LocalDateTimeUtil.parse(
                  startDate.concat(" ").concat(reqVO.getEndTime()), "yyyy-MM-dd HH:mm"));
          if (reqVO.getNextDay()) {
            dplan.setEndTime(dplan.getEndTime().plusDays(1));
          }
          dplan.setConfigDate(LocalDateTimeUtil.format(start, "yyyy-MM-dd"));
          this.save(dplan);
        }
        start = start.plusDays(1);
      }
    }
  }

  @Override
  public void deleteByMachinNoAndRange(String machineNo, String startDate, String endDate) {
    xhfMachineDisablePlanMapper.deleteByMachinNoAndRange(machineNo, startDate, endDate);
  }

  @Override
  public List<MachineDisableInfoDTO> getMachineDisablePlan(String machineNo, String day) {
    List<MachineDisableInfoDTO> result = Lists.newArrayList();
    LocalDateTime start = LocalDateTimeUtil.parse(day, "yyyy-MM");
    List<XhfMachineDisablePlanDO> r =
        getByMachineNo(machineNo, start.plusDays(-10), start.plusDays(40));
    r.forEach(
        i -> {
          MachineDisableInfoDTO e = new MachineDisableInfoDTO();
          e.setYear(i.getConfigDate().substring(0, 4));
          e.setMonth(i.getConfigDate().substring(5, 7));
          e.setDay(i.getConfigDate().substring(8, 10));
          String info = LocalDateTimeUtil.format(i.getStartTime(), "HH:mm");
          info = info.concat("~");
          if (i.getEndTime().getDayOfMonth() != i.getStartTime().getDayOfMonth()) {
            info = info.concat("次日");
          }
          info = info.concat(LocalDateTimeUtil.format(i.getEndTime(), "HH:mm"));
          e.setInfo(info);
          result.add(e);
        });
    return result;
  }
}
