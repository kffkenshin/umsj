package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderDetailDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 生产订单明细 Mapper 接口
 *
 * @author ethan
 * @since 2024-10-23
 */
public interface XhfManufactureOrderDetailMapper
    extends YesBaseMapper<XhfManufactureOrderDetailDO> {

  default List<XhfManufactureOrderDetailDO> getByHeaderIds(List<Long> headerIds) {
    LambdaQueryWrapperX<XhfManufactureOrderDetailDO> query =
        new LambdaQueryWrapperX<XhfManufactureOrderDetailDO>();
    query.in(XhfManufactureOrderDetailDO::getHeaderId, headerIds);
    query.orderByAsc(XhfManufactureOrderDetailDO::getWorkStation);
    return selectList(query);
  }

  default List<XhfManufactureOrderDetailDO> getByMachinNoAndStartTime(
      String machineNo, LocalDateTime beginTime) {
    LambdaQueryWrapperX<XhfManufactureOrderDetailDO> query =
        new LambdaQueryWrapperX<XhfManufactureOrderDetailDO>();
    query.in(XhfManufactureOrderDetailDO::getMachineNumber, machineNo);
    query.gt(XhfManufactureOrderDetailDO::getStartTime, beginTime);
    return selectList(query);
  }

  default XhfManufactureOrderDetailDO getLastDetail(XhfManufactureOrderDetailDO moDetail) {
    LambdaQueryWrapperX<XhfManufactureOrderDetailDO> query =
        new LambdaQueryWrapperX<XhfManufactureOrderDetailDO>();
    query.eq(XhfManufactureOrderDetailDO::getWorkStation, moDetail.getWorkStation());
    query.eq(XhfManufactureOrderDetailDO::getMachineNumber, moDetail.getMachineNumber());
    query.lt(XhfManufactureOrderDetailDO::getStartTime, moDetail.getStartTime());
    query.orderByDesc(XhfManufactureOrderDetailDO::getStartTime);
    return selectOne(query);
  }

  default List<XhfManufactureOrderDetailDO> getDetailsToPlan(
      Integer workStation, String machineNo, LocalDateTime start, LocalDateTime end) {
    LambdaQueryWrapperX<XhfManufactureOrderDetailDO> query =
        new LambdaQueryWrapperX<XhfManufactureOrderDetailDO>();
    query.eq(XhfManufactureOrderDetailDO::getMachineNumber, machineNo);
    query.eq(XhfManufactureOrderDetailDO::getWorkStation, workStation);
    query.gt(XhfManufactureOrderDetailDO::getStartTime, start);
    query.lt(XhfManufactureOrderDetailDO::getStartTime, end);
    query.orderByAsc(XhfManufactureOrderDetailDO::getStartTime);
    return selectList(query);
  }
  ;
}
