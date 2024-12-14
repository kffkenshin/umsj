package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryListVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductBomDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessRouteDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductBomDTO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.MPJLambdaWrapperX;
import java.util.List;

/**
 * BOM表 Mapper 接口
 *
 * @author ethan
 * @since 2024-09-21
 */
public interface XhfProductBomMapper extends YesBaseMapper<XhfProductBomDO> {

  default List<XhfProductBomDTO> listQuery(ItemQueryListVO reqVO) {
    return getByItemId(reqVO.getItemId());
  }

  default List<XhfProductBomDTO> getByItemId(Long itemId) {
    MPJLambdaWrapperX<XhfProductBomDO> query = new MPJLambdaWrapperX<XhfProductBomDO>();
    query.selectAs("t.seq", XhfProductBomDTO::getSeq);
    query.selectAs("item.item_no", XhfProductBomDTO::getMaterialNo);
    query.selectAs("item.item_name", XhfProductBomDTO::getMaterialName);
    query.selectAs("item.item_spec", XhfProductBomDTO::getMaterialSpec);
    query.selectAs("item.tickness", XhfProductBomDTO::getMaterialTickness);
    query.selectAs("item.width", XhfProductBomDTO::getMaterialWidth);
    query.selectAs("item.length", XhfProductBomDTO::getMaterialLength);
    query.selectAs("t.base_number", XhfProductBomDTO::getBaseNumber);
    query.selectAs("process.process_no", XhfProductBomDTO::getProcessNo);
    query.selectAs("process.process_name", XhfProductBomDTO::getProcessName);
    query.leftJoin(XhfItemDO.class, "item", XhfItemDO::getId, XhfProductBomDO::getMaterialId);
    query.leftJoin(
        XhfProductProcessRouteDO.class,
        "route",
        XhfProductProcessRouteDO::getId,
        XhfProductBomDO::getProcessRouteId);
    query.leftJoin(
        XhfProductProcessDO.class,
        "process",
        XhfProductProcessDO::getId,
        XhfProductProcessRouteDO::getProcessId);
    query.eq(XhfProductBomDO::getItemId, itemId);
    query.orderByAsc(XhfProductBomDO::getSeq);
    return selectJoinList(XhfProductBomDTO.class, query);
  }
  ;
}
