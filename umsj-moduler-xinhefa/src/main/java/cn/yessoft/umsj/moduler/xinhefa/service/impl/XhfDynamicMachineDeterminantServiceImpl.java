package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.DynamicMachineQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfDynamicMachineDeterminantDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfDynamicMachineDeterminantMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfDynamicMachineDeterminantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 规则选机 服务实现类
 *
 * @author ethan
 * @since 2024-10-19
 */
@Service("xhfDynamicMachineDeterminantService")
public class XhfDynamicMachineDeterminantServiceImpl
    extends ServiceImpl<XhfDynamicMachineDeterminantMapper, XhfDynamicMachineDeterminantDO>
    implements IXhfDynamicMachineDeterminantService {
  @Resource private XhfDynamicMachineDeterminantMapper xhfDynamicMachineDeterminantMapper;

  @Override
  public PageResult<XhfDynamicMachineDeterminantDO> pagedQuery(DynamicMachineQueryReqVO reqVO) {
    return xhfDynamicMachineDeterminantMapper.pagedQuery(reqVO);
  }
}
