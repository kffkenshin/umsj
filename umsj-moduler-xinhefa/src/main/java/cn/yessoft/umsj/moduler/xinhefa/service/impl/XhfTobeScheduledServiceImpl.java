package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfTobeScheduledDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfTobeScheduledMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfTobeScheduledService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 待排产机台 服务实现类
 *
 * @author ethan
 * @since 2024-11-07
 */
@Service("xhfTobeScheduledService")
public class XhfTobeScheduledServiceImpl
    extends ServiceImpl<XhfTobeScheduledMapper, XhfTobeScheduledDO>
    implements IXhfTobeScheduledService {
  @Resource private XhfTobeScheduledMapper xhfTobeScheduledMapper;

  @Override
  public XhfTobeScheduledDO getFirst() {
    return xhfTobeScheduledMapper.getFirst();
  }

  @Override
  public void addScheduler(Integer workStation, String machineNumber, String modeNo) {
    XhfTobeScheduledDO tobe = xhfTobeScheduledMapper.getByWsAndMachine(workStation, machineNumber);
    if (tobe == null) {
      tobe = new XhfTobeScheduledDO();
      tobe.setWorkStation(workStation);
      tobe.setMode(modeNo);
      tobe.setMachineNo(machineNumber);
      xhfTobeScheduledMapper.insert(tobe);
    } else if (!tobe.getMode().equals(modeNo)) {
      tobe.setMode(modeNo);
      xhfTobeScheduledMapper.updateById(tobe);
    }
  }

  @Override
  @Transactional
  public void removeScheduler(Integer workStation, String machineNumber) {
    xhfTobeScheduledMapper.removeScheduler(workStation, machineNumber);
  }
}
