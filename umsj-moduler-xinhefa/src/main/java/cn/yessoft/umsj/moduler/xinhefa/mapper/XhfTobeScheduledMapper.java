package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfTobeScheduledDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 待排产机台 Mapper 接口
 *
 * @author ethan
 * @since 2024-11-07
 */
public interface XhfTobeScheduledMapper extends YesBaseMapper<XhfTobeScheduledDO> {

  default XhfTobeScheduledDO getFirst() {
    return selectOne(
        new LambdaQueryWrapperX<XhfTobeScheduledDO>()
            .orderByAsc(XhfTobeScheduledDO::getWorkStation));
  }

  default XhfTobeScheduledDO getByWsAndMachine(Integer workStation, String machineNumber) {
    return selectOne(
        new LambdaQueryWrapperX<XhfTobeScheduledDO>()
            .eq(XhfTobeScheduledDO::getWorkStation, workStation)
            .eq(XhfTobeScheduledDO::getMachineNo, machineNumber));
  }

  default void removeScheduler(Integer workStation, String machineNumber) {
    delete(
        new LambdaQueryWrapperX<XhfTobeScheduledDO>()
            .eq(XhfTobeScheduledDO::getWorkStation, workStation)
            .eq(XhfTobeScheduledDO::getMachineNo, machineNumber));
  }
}
