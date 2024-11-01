package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachinePropertyDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfMachinePropertyMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachinePropertyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 机台产能参数 服务实现类
 *
 * @author ethan
 * @since 2024-10-17
 */
@Service("xhfMachinePropertyService")
public class XhfMachinePropertyServiceImpl
    extends ServiceImpl<XhfMachinePropertyMapper, XhfMachinePropertyDO>
    implements IXhfMachinePropertyService {
  @Resource private XhfMachinePropertyMapper xhfMachinePropertyMapper;

  @Override
  public List<XhfMachinePropertyDO> getListByMachineId(Long id) {
    return xhfMachinePropertyMapper.getListByMachineId(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteByMachineId(Long i) {
    xhfMachinePropertyMapper.delete(XhfMachinePropertyDO::getMachineId, i);
  }

  @Override
  @Transactional
  public void delete(List<Long> ids) {
    xhfMachinePropertyMapper.deleteBatchIds(ids);
  }
}
