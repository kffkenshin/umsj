package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderDeliverCreateVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSaleOrderDeliverDO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 订单批次 服务类
 *
 * @author ethan
 * @since 2024-10-15
 */
public interface IXhfSaleOrderDeliverService extends IService<XhfSaleOrderDeliverDO> {

  void batchCreate(List<SaleOrderDeliverCreateVO> reqVO);
}
