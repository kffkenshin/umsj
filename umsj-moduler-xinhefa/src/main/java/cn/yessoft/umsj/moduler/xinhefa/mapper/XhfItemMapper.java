package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import java.util.List;

/**
 * 产品类型表 Mapper 接口
 *
 * @author ethan
 * @since 2024-09-19
 */
public interface XhfItemMapper extends YesBaseMapper<XhfItemDO> {

  default PageResult<XhfItemDO> pagedQuery(ItemQueryVO reqVO) {
    return selectPage(
        reqVO,
        new LambdaQueryWrapperX<XhfItemDO>()
            .inIfPresent(XhfItemDO::getItemType1, reqVO.getItemType())
            .inIfPresent(XhfItemDO::getItemType2, reqVO.getItemType2())
            .and(
                BaseUtils.isNotEmpty(reqVO.getInfo()),
                w ->
                    w.like(XhfItemDO::getItemNo, reqVO.getInfo())
                        .or()
                        .like(XhfItemDO::getItemName, reqVO.getInfo())
                        .or()
                        .like(XhfItemDO::getItemSpec, reqVO.getInfo())));
  }

  default List<XhfItemDO> listQuery(ItemQueryVO reqVO) {
    return selectList(
        new LambdaQueryWrapperX<XhfItemDO>()
            .likeIfPresent(XhfItemDO::getItemNo, reqVO.getInfo())
            .or()
            .likeIfPresent(XhfItemDO::getItemName, reqVO.getInfo())
            .or()
            .likeIfPresent(XhfItemDO::getItemSpec, reqVO.getInfo()));
  }

  default XhfItemDO getItemByNo(String itemNo) {
    return selectOne(XhfItemDO::getItemNo, itemNo);
  }

  default List<XhfItemDO> getEmptyType2Items() {
    return selectList(
        new LambdaQueryWrapperX<XhfItemDO>()
            .likeRight(XhfItemDO::getItemNo, "1")
            .isNull(XhfItemDO::getItemType2));
  }

  default List<XhfItemDO> getEmptyChannelItems() {
    return selectList(
        new LambdaQueryWrapperX<XhfItemDO>()
            .likeRight(XhfItemDO::getItemNo, "1")
            .isNull(XhfItemDO::getChannelsCount));
  }
}
