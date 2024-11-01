package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderHeaderDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderLogDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfManufactureOrderLogMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfManufactureOrderLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 生产订单日志 服务实现类
 *
 * @author ethan
 * @since 2024-10-23
 */
@Service("xhfMoLogService")
public class XhfManufactureOrderLogServiceImpl
    extends ServiceImpl<XhfManufactureOrderLogMapper, XhfManufactureOrderLogDO>
    implements IXhfManufactureOrderLogService {
  @Override
  public void createMoLog(XhfManufactureOrderHeaderDO moHeader, String action, String log) {
    XhfManufactureOrderLogDO logDO = new XhfManufactureOrderLogDO();
    logDO.setLog(log);
    logDO.setHeaderId(moHeader.getId());
    logDO.setAction(action);
    this.save(logDO);
  }
}
