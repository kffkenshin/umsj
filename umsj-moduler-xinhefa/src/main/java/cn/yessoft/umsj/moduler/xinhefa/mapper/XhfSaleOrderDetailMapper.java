package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderDetailQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfSaleOrderDetailDTO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.MPJLambdaWrapperX;
import java.util.Arrays;
import java.util.List;

/**
 * 销售订单明细表 Mapper 接口
 *
 * @author ethan
 * @since 2024-10-03
 */
public interface XhfSaleOrderDetailMapper extends YesBaseMapper<XhfSaleOrderDetailDO> {

  default PageResult<XhfSaleOrderDetailDTO> pagedQuery(SaleOrderDetailQueryReqVO reqVO) {
    MPJLambdaWrapperX<XhfSaleOrderDetailDO> query = new MPJLambdaWrapperX<>();
    query.selectAll(XhfSaleOrderDetailDO.class);
    query.selectAs("header.order_date", XhfSaleOrderDetailDTO::getOrderDate);
    query.selectAs("customer.name_short", XhfSaleOrderDetailDTO::getCusNameShort);
    query.selectAs("cusitem.customer_item_no", XhfSaleOrderDetailDTO::getCusItemNo);
    query.selectAs("item.item_name", XhfSaleOrderDetailDTO::getItemName);
    query.selectAs("item.item_no", XhfSaleOrderDetailDTO::getItemNo);
    query.selectAs("item.item_type1", XhfSaleOrderDetailDTO::getItemType);
    query.selectAs("item.item_type2", XhfSaleOrderDetailDTO::getItemType2);
    query.selectAs("header.cus_sale_order_no", XhfSaleOrderDetailDTO::getCustomerOrderNo);
    query.leftJoin(
        XhfSaleOrderHeaderDO.class,
        "header",
        XhfSaleOrderHeaderDO::getId,
        XhfSaleOrderDetailDO::getSoId);
    query.leftJoin(XhfItemDO.class, "item", XhfItemDO::getId, XhfSaleOrderDetailDO::getItemId);
    query.leftJoin(
        XhfCustomerItemDO.class,
        "cusitem",
        XhfCustomerItemDO::getId,
        XhfSaleOrderDetailDO::getCusItemId);
    query.leftJoin(
        XhfCustomerDO.class, "customer", XhfCustomerDO::getId, XhfSaleOrderHeaderDO::getCustomerId);

    query.inIfPresent(XhfItemDO::getItemType2, reqVO.getItemType2());
    query.inIfPresent(XhfSaleOrderDetailDO::getApsStatus, reqVO.getApsStatus());
    if (BaseUtils.isNotEmpty(reqVO.getSoNumbers())) {
      List<String> soNumbers = Arrays.asList(BaseUtils.toString(reqVO.getSoNumbers()).split(" "));
      query.inIfPresent(XhfSaleOrderDetailDO::getSoDetailNumber, soNumbers);
    }
    query.and(
        BaseUtils.isNotEmpty(reqVO.getCustomerInfo()),
        w ->
            w.like(XhfCustomerDO::getNo, reqVO.getCustomerInfo())
                .or()
                .like(XhfCustomerDO::getNameCn, reqVO.getCustomerInfo())
                .or()
                .like(XhfCustomerDO::getNameShort, reqVO.getCustomerInfo()));
    query.and(
        BaseUtils.isNotEmpty(reqVO.getItemInfo()),
        w ->
            w.like(XhfItemDO::getItemNo, reqVO.getItemInfo())
                .or()
                .like(XhfItemDO::getItemSpec, reqVO.getItemInfo())
                .or()
                .like(XhfItemDO::getItemName, reqVO.getItemInfo())
                .or()
                .like(XhfCustomerItemDO::getCustomerItemName, reqVO.getItemInfo())
                .or()
                .like(XhfCustomerItemDO::getCustomerItemSpec, reqVO.getItemInfo())
                .or()
                .like(XhfCustomerItemDO::getCustomerItemNo, reqVO.getItemInfo()));
    if (BaseUtils.isNotEmpty(reqVO.getOrderDate()) && reqVO.getOrderDate().size() > 1) {
      query.betweenIfPresent(
          XhfSaleOrderHeaderDO::getOrderDate,
          reqVO.getOrderDate().get(0),
          reqVO.getOrderDate().get(1));
    }
    if (BaseUtils.isNotEmpty(reqVO.getPreDeleverDate()) && reqVO.getPreDeleverDate().size() > 1) {
      query.betweenIfPresent(
          XhfSaleOrderDetailDTO::getPreDeliveryDate,
          reqVO.getPreDeleverDate().get(0),
          reqVO.getPreDeleverDate().get(1));
    }
    query.orderByDesc(XhfSaleOrderHeaderDO::getOrderDate);
    return selectJoinPage(reqVO, XhfSaleOrderDetailDTO.class, query);
  }
  ;
}
