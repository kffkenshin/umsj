package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderHeaderDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderLogDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 生产订单日志 服务类
 *
 * @author ethan
 * @since 2024-10-23
 */
public interface IXhfManufactureOrderLogService extends IService<XhfManufactureOrderLogDO> {

  void createMoLog(XhfManufactureOrderHeaderDO moHeader, String action, String log);
}
