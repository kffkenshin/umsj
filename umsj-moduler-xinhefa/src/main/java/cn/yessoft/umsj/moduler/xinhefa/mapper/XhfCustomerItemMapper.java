package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfCustomerItemDTO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.MPJLambdaWrapperX;

/**
 * 客户物料表 Mapper 接口
 *
 * @author ethan
 * @since 2024-09-24
 */
public interface XhfCustomerItemMapper extends YesBaseMapper<XhfCustomerItemDO> {

  default PageResult<XhfCustomerItemDTO> pagedQuery(InfoPageQueryReqVO reqVO) {
    MPJLambdaWrapperX<XhfCustomerItemDO> query = new MPJLambdaWrapperX<XhfCustomerItemDO>();
    query.selectAs("customer_item_no", XhfCustomerItemDTO::getCustomerItemNo);
    query.selectAs("customer_item_name", XhfCustomerItemDTO::getCustomerItemName);
    query.selectAs("customer_item_spec", XhfCustomerItemDTO::getCustomerItemSpec);
    query.selectAs("item.item_name", XhfCustomerItemDTO::getItemName);
    query.selectAs("item.item_no", XhfCustomerItemDTO::getItemNo);
    query.selectAs("item.item_spec", XhfCustomerItemDTO::getItemSpec);
    query.selectAs("cus.name_cn", XhfCustomerItemDTO::getCustomerName);
    query.leftJoin(XhfItemDO.class, "item", XhfItemDO::getId, XhfCustomerItemDO::getItemId);
    query.leftJoin(
        XhfCustomerDO.class, "cus", XhfCustomerDO::getId, XhfCustomerItemDO::getCusomerId);
    query
        .likeIfExists(XhfCustomerItemDO::getCustomerItemNo, reqVO.getInfo())
        .or()
        .likeIfExists(XhfCustomerItemDO::getCustomerItemName, reqVO.getInfo())
        .or()
        .likeIfExists(XhfCustomerItemDO::getCustomerItemSpec, reqVO.getInfo())
        .or()
        .likeIfExists(XhfItemDO::getItemNo, reqVO.getInfo())
        .or()
        .likeIfExists(XhfItemDO::getItemName, reqVO.getInfo())
        .or()
        .likeIfExists(XhfItemDO::getItemSpec, reqVO.getInfo())
        .or()
        .likeIfExists(XhfCustomerDO::getNameCn, reqVO.getInfo());
    query.orderByAsc(XhfCustomerItemDO::getItemId);
    return selectJoinPage(reqVO, XhfCustomerItemDTO.class, query);
  }

  default XhfCustomerItemDO getCustomerItem(
      String customerItemNo, String itemNo, String customerNo) {
    MPJLambdaWrapperX<XhfCustomerItemDO> query = new MPJLambdaWrapperX<XhfCustomerItemDO>();
    query.leftJoin(XhfItemDO.class, "item", XhfItemDO::getId, XhfCustomerItemDO::getItemId);
    query.leftJoin(
        XhfCustomerDO.class, "cus", XhfCustomerDO::getId, XhfCustomerItemDO::getCusomerId);
    query.eq(XhfCustomerItemDO::getCustomerItemNo, customerItemNo);
    query.eq(XhfItemDO::getItemNo, itemNo);
    query.eq(XhfCustomerDO::getNo, customerNo);
    return selectJoinOne(XhfCustomerItemDO.class, query);
  }
  ;
}
