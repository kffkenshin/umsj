package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderBatchDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import java.util.List;

/**
 * 生产订单批次 Mapper 接口
 *
 * @author ethan
 * @since 2024-10-23
 */
public interface XhfManufactureOrderBatchMapper extends YesBaseMapper<XhfManufactureOrderBatchDO> {

  default List<XhfManufactureOrderBatchDO> getByHeaderIds(List<Long> headerIds) {
    LambdaQueryWrapperX<XhfManufactureOrderBatchDO> query =
        new LambdaQueryWrapperX<XhfManufactureOrderBatchDO>();
    query.in(XhfManufactureOrderBatchDO::getHeaderId, headerIds);
    query.orderByAsc(XhfManufactureOrderBatchDO::getBatchNo);
    return selectList(query);
  }
  ;
}
