package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.DynamicMachineQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfDynamicMachineDeterminantDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 规则选机 服务类
 *
 * @author ethan
 * @since 2024-10-19
 */
public interface IXhfDynamicMachineDeterminantService
    extends IService<XhfDynamicMachineDeterminantDO> {

  PageResult<XhfDynamicMachineDeterminantDO> pagedQuery(DynamicMachineQueryReqVO reqVO);
}
