package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDisablePlanDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 停机计划 Mapper 接口
 *
 * @author ethan
 * @since 2024-11-07
 */
public interface XhfMachineDisablePlanMapper extends YesBaseMapper<XhfMachineDisablePlanDO> {

  default List<XhfMachineDisablePlanDO> getByMachineNo(
      String machineNo, LocalDateTime start, LocalDateTime end) {
    return selectList(
        new LambdaQueryWrapperX<XhfMachineDisablePlanDO>()
            .eq(XhfMachineDisablePlanDO::getMachineNumber, machineNo)
            .gt(XhfMachineDisablePlanDO::getStartTime, start)
            .lt(XhfMachineDisablePlanDO::getStartTime, end));
  }

  default void deleteByMachinNoAndRange(String machineNo, String startDate, String endDate) {
    delete(
        new LambdaQueryWrapperX<XhfMachineDisablePlanDO>()
            .eq(XhfMachineDisablePlanDO::getMachineNumber, machineNo)
            .gt(XhfMachineDisablePlanDO::getConfigDate, startDate)
            .lt(XhfMachineDisablePlanDO::getConfigDate, endDate));
  }
  ;
}
