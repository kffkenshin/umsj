package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSoDeliverViewDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import java.util.Arrays;
import java.util.List;

/**
 * VIEW Mapper 接口
 *
 * @author ethan
 * @since 2024-10-16
 */
public interface XhfSoDeliverViewMapper extends YesBaseMapper<XhfSoDeliverViewDO> {

  default PageResult<XhfSoDeliverViewDO> pagedQuery(SaleOrderQueryReqVO reqVO) {
    LambdaQueryWrapperX<XhfSoDeliverViewDO> query = new LambdaQueryWrapperX<>();
    if (BaseUtils.isNotEmpty(reqVO.getSoNumbers())) {
      List<String> soNumbers = Arrays.asList(BaseUtils.toString(reqVO.getSoNumbers()).split(" "));
      query.inIfPresent(XhfSoDeliverViewDO::getDetailNumber, soNumbers);
    }
    query.inIfPresent(XhfSoDeliverViewDO::getItemType, reqVO.getItemType2());
    query.inIfPresent(XhfSoDeliverViewDO::getDeliverStatus, reqVO.getApsStatus());
    query.and(
        BaseUtils.isNotEmpty(reqVO.getCustomerInfo()),
        w -> w.like(XhfSoDeliverViewDO::getCusNameShort, reqVO.getCustomerInfo()));
    query.and(
        BaseUtils.isNotEmpty(reqVO.getItemInfo()),
        w ->
            w.like(XhfSoDeliverViewDO::getItemNo, reqVO.getItemInfo())
                .or()
                .like(XhfSoDeliverViewDO::getItemName, reqVO.getItemInfo())
                .or()
                .like(XhfSoDeliverViewDO::getCustomerItemNo, reqVO.getItemInfo()));
    if (BaseUtils.isNotEmpty(reqVO.getOrderDate()) && reqVO.getOrderDate().size() > 1) {
      query.betweenIfPresent(
          XhfSoDeliverViewDO::getOrderDate,
          reqVO.getOrderDate().get(0),
          reqVO.getOrderDate().get(1));
    }
    if (BaseUtils.isNotEmpty(reqVO.getPreDeleverDate()) && reqVO.getPreDeleverDate().size() > 1) {
      query.betweenIfPresent(
          XhfSoDeliverViewDO::getDeliverDate,
          reqVO.getPreDeleverDate().get(0),
          reqVO.getPreDeleverDate().get(1));
    }
    query.orderByDesc(XhfSoDeliverViewDO::getOrderDate);
    return selectPage(reqVO, query);
  }
  ;
}
