package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.MachineDisablePlanCreateVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDisablePlanDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MachineDisableInfoDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停机计划 服务类
 *
 * @author ethan
 * @since 2024-11-07
 */
public interface IXhfMachineDisablePlanService extends IService<XhfMachineDisablePlanDO> {

  List<XhfMachineDisablePlanDO> getByMachineNo(
      String machineNo, LocalDateTime beginOfThisWeek, LocalDateTime localDateTime);

  void createPlans(MachineDisablePlanCreateVO reqVO);

  void deleteByMachinNoAndRange(String machineNo, String startDate, String endDate);

  List<MachineDisableInfoDTO> getMachineDisablePlan(String machineNo, String day);
}
