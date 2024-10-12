package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSaleOrderHeaderDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 销售订单表 服务类
 *
 * @author ethan
 * @since 2024-10-03
 */
public interface IXhfSaleOrderHeaderService extends IService<XhfSaleOrderHeaderDO> {
  XhfSaleOrderHeaderDO getByOrderNo(String orderNo);
}
