package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfMachineMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachinePropertyService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 机台表 服务实现类
 *
 * @author ethan
 * @since 2024-10-17
 */
@Service("xhfMachineService")
public class XhfMachineServiceImpl extends ServiceImpl<XhfMachineMapper, XhfMachineDO>
    implements IXhfMachineService {
  @Resource private XhfMachineMapper xhfMachineMapper;
  @Resource private IXhfMachinePropertyService xhfMachinePropertyService;

  @Override
  public PageResult<XhfMachineDO> pagedQuery(InfoPageQueryReqVO reqVO) {
    return xhfMachineMapper.pagedQuery(reqVO);
  }

  @Override
  public void delete(List<Long> ids) {
    ids.forEach(
        i -> {
          XhfMachineDO machine = xhfMachineMapper.selectById(i);
          if (machine != null) {
            xhfMachinePropertyService.deleteByMachineId(i);
            xhfMachineMapper.deleteById(i);
          }
        });
  }
}
