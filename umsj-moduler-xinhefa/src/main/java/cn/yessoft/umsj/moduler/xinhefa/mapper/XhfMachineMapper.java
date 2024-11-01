package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 机台表 Mapper 接口
 *
 * @author ethan
 * @since 2024-10-17
 */
public interface XhfMachineMapper extends YesBaseMapper<XhfMachineDO> {

  default PageResult<XhfMachineDO> pagedQuery(InfoPageQueryReqVO reqVO) {
    return selectPage(
        reqVO,
        new LambdaQueryWrapperX<XhfMachineDO>()
            .likeIfPresent(XhfMachineDO::getMachineNo, reqVO.getInfo())
            .or()
            .likeIfPresent(XhfMachineDO::getMachineName, reqVO.getInfo())
            .orderByAsc(XhfMachineDO::getWorkStation)
            .orderByAsc(XhfMachineDO::getSeq));
  }
  ;
}
