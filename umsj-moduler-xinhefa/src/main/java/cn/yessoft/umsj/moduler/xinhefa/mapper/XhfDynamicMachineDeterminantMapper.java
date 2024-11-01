package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.DynamicMachineQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfDynamicMachineDeterminantDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 规则选机 Mapper 接口
 *
 * @author ethan
 * @since 2024-10-19
 */
public interface XhfDynamicMachineDeterminantMapper
    extends YesBaseMapper<XhfDynamicMachineDeterminantDO> {

  default PageResult<XhfDynamicMachineDeterminantDO> pagedQuery(DynamicMachineQueryReqVO reqVO) {
    return selectPage(
        reqVO,
        new LambdaQueryWrapperX<XhfDynamicMachineDeterminantDO>()
            .inIfPresent(XhfDynamicMachineDeterminantDO::getWorkStation, reqVO.getWorkStation()));
  }
  ;
}
