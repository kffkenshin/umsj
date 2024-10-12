package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryListVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductProcessRouteDTO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.MPJLambdaWrapperX;
import java.util.List;

/**
 * 工艺路线表 Mapper 接口
 *
 * @author ethan
 * @since 2024-09-21
 */
public interface XhfProductProcessRouteMapper extends YesBaseMapper<XhfProductProcessRouteDO> {

  default List<XhfProductProcessRouteDTO> listQuery(ItemQueryListVO reqVO) {
    MPJLambdaWrapperX<XhfProductProcessRouteDO> query =
        new MPJLambdaWrapperX<XhfProductProcessRouteDO>();
    query.selectAs("t.seq", XhfProductProcessRouteDTO::getSeq);
    query.selectAs("process.process_no", XhfProductProcessRouteDTO::getProcessNo);
    query.selectAs("process.process_name", XhfProductProcessRouteDTO::getProcessName);
    query.selectAs("process.work_station", XhfProductProcessRouteDTO::getWorkStation);
    query.leftJoin(
        XhfProductProcessDO.class,
        "process",
        XhfProductProcessDO::getId,
        XhfProductProcessRouteDO::getProcessId);
    query.eq(XhfProductProcessRouteDO::getItemId, reqVO.getItemId());
    query.orderByAsc(XhfProductProcessRouteDO::getSeq);
    return selectJoinList(XhfProductProcessRouteDTO.class, query);
  }

  default List<SimulateDetailDTO> listByitemId(Long itemId) {
    MPJLambdaWrapperX<XhfProductProcessRouteDO> query =
        new MPJLambdaWrapperX<XhfProductProcessRouteDO>();
    query.selectAs("t.seq", SimulateDetailDTO::getSeq);
    query.selectAs("process.process_no", SimulateDetailDTO::getProcessNo);
    query.selectAs("process.process_name", SimulateDetailDTO::getProcessName);
    query.selectAs("process.prepare_reject_rate", SimulateDetailDTO::getPrepareQty1);
    query.selectAs("process.process_reject_rate", SimulateDetailDTO::getProcessRejectRate);
    query.selectAs("process.abnormal_reject_rate", SimulateDetailDTO::getAbnormalRejectRate);
    query.selectAs("process.work_station", SimulateDetailDTO::getWorkStation);
    query.leftJoin(
        XhfProductProcessDO.class,
        "process",
        XhfProductProcessDO::getId,
        XhfProductProcessRouteDO::getProcessId);
    query.eq(XhfProductProcessRouteDO::getItemId, itemId);
    query.orderByAsc(XhfProductProcessRouteDO::getSeq);
    return selectJoinList(SimulateDetailDTO.class, query);
  }

  default XhfProductProcessRouteDO getByItemIdAndProcessNo(Long itemId, String processNo) {
    MPJLambdaWrapperX<XhfProductProcessRouteDO> query =
        new MPJLambdaWrapperX<XhfProductProcessRouteDO>();
    query.leftJoin(
        XhfProductProcessDO.class,
        "process",
        XhfProductProcessDO::getId,
        XhfProductProcessRouteDO::getProcessId);
    query.eq(XhfProductProcessRouteDO::getItemId, itemId);
    query.eq(XhfProductProcessDO::getProcessNo, processNo);
    return selectJoinOne(XhfProductProcessRouteDO.class, query);
  }
  ;
}
