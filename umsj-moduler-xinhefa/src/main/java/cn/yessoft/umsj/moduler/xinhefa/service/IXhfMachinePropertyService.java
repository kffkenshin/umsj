package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachinePropertyDO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 机台产能参数 服务类
 *
 * @author ethan
 * @since 2024-10-17
 */
public interface IXhfMachinePropertyService extends IService<XhfMachinePropertyDO> {

  List<XhfMachinePropertyDO> getListByMachineId(Long id);

  void deleteByMachineId(Long i);

  void delete(List<Long> ids);
}
