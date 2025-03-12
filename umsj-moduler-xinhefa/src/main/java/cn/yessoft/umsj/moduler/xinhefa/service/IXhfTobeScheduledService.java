package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfTobeScheduledDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 待排产机台 服务类
 *
 * @author ethan
 * @since 2024-11-07
 */
public interface IXhfTobeScheduledService extends IService<XhfTobeScheduledDO> {

  XhfTobeScheduledDO getFirst();

  void addScheduler(Integer workStation, String machineNumber, String modeNo);

  @Transactional
  void removeScheduler(Integer workStation, String machineNumber);
}
