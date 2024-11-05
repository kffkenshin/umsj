package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.mo.MoHeaderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MoHeaderDTO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.MPJLambdaWrapperX;
import java.util.Arrays;
import java.util.List;

/**
 * 生产订单 Mapper 接口
 *
 * @author ethan
 * @since 2024-10-23
 */
public interface XhfManufactureOrderHeaderMapper
    extends YesBaseMapper<XhfManufactureOrderHeaderDO> {

  default PageResult<MoHeaderDTO> pagedQuery(MoHeaderQueryReqVO reqVO) {
    MPJLambdaWrapperX<XhfManufactureOrderHeaderDO> query = new MPJLambdaWrapperX<>();
    query.selectAs("t.id", MoHeaderDTO::getId);
    query.selectAs("t.order_no", MoHeaderDTO::getOrderNo);
    query.selectAs("t.mo_date", MoHeaderDTO::getMoDate);
    query.selectAs("t.qty", MoHeaderDTO::getQty);
    query.selectAs("t.status", MoHeaderDTO::getApsStatus);
    query.selectAs("t.remark", MoHeaderDTO::getMoRemark);
    query.selectAs("t.so_detail_number", MoHeaderDTO::getSoDetailNumber);
    query.selectAs("t.addtion_rate", MoHeaderDTO::getAddtionRate);
    query.selectAs("customer.name_short", MoHeaderDTO::getCusNameShort);
    query.selectAs("item.item_name", MoHeaderDTO::getItemName);
    query.selectAs("item.item_no", MoHeaderDTO::getItemNo);
    query.selectAs("item.item_type1", MoHeaderDTO::getItemType);
    query.selectAs("item.item_type2", MoHeaderDTO::getItemType2);
    query.selectAs("item.g_weight", MoHeaderDTO::getItemGWeight);
    query.selectAs("item.length", MoHeaderDTO::getItemLength);
    query.selectAs("item.sale_unit", MoHeaderDTO::getPurchasingNuit);
    query.leftJoin(
        XhfItemDO.class, "item", XhfItemDO::getId, XhfManufactureOrderHeaderDO::getItemId);
    query.leftJoin(
        XhfCustomerDO.class,
        "customer",
        XhfCustomerDO::getId,
        XhfManufactureOrderHeaderDO::getCustomerId);
    query.inIfPresent(XhfItemDO::getItemType2, reqVO.getItemType2());
    query.inIfPresent(XhfManufactureOrderHeaderDO::getStatus, reqVO.getApsStatus());
    if (BaseUtils.isNotEmpty(reqVO.getDocNumbers())) {
      List<String> docNumbers = Arrays.asList(BaseUtils.toString(reqVO.getDocNumbers()).split(" "));
      query.inIfPresent(XhfManufactureOrderHeaderDO::getOrderNo, docNumbers);
      query.and(
          w -> {
            w.in(XhfManufactureOrderHeaderDO::getOrderNo, docNumbers)
                .or()
                .in(XhfManufactureOrderHeaderDO::getSoDetailNumber, docNumbers);
          });
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
                .like(XhfItemDO::getItemName, reqVO.getItemInfo()));
    if (BaseUtils.isNotEmpty(reqVO.getOrderDate()) && reqVO.getOrderDate().size() > 1) {
      query.betweenIfPresent(
          XhfManufactureOrderHeaderDO::getMoDate,
          reqVO.getOrderDate().get(0),
          reqVO.getOrderDate().get(1));
    }
    // todo 计划权限
    // todo 快速筛选
    return selectJoinPage(reqVO, MoHeaderDTO.class, query);
  }

  default List<XhfManufactureOrderHeaderDO> getHeadersByStatus(String statusNo) {
    return selectList(XhfManufactureOrderHeaderDO::getStatus, statusNo);
  }
}
