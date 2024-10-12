package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSaleOrderHeaderDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfSaleOrderHeaderMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSaleOrderHeaderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 销售订单表 服务实现类
 *
 * @author ethan
 * @since 2024-10-03
 */
@Service("xhfSaleOrderHeaderService")
public class XhfSaleOrderHeaderServiceImpl
    extends ServiceImpl<XhfSaleOrderHeaderMapper, XhfSaleOrderHeaderDO>
    implements IXhfSaleOrderHeaderService {
  @Resource private XhfSaleOrderHeaderMapper xhfSaleOrderMapper;

  @Override
  public XhfSaleOrderHeaderDO getByOrderNo(String orderNo) {
    return xhfSaleOrderMapper.selectOne(XhfSaleOrderHeaderDO::getOrderNo, orderNo);
  }
}
