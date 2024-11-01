package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderDetailDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
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
  ;
}
