package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSaleOrderDetailDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfSaleOrderDetailDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 销售订单明细表 服务类
 *
 * @author ethan
 * @since 2024-10-03
 */
public interface IXhfSaleOrderDetailService extends IService<XhfSaleOrderDetailDO> {

  XhfSaleOrderDetailDO getBySOIDAndSeq(Long id, Integer detailSeq);

  PageResult<XhfSaleOrderDetailDTO> pagedQuery(SaleOrderQueryReqVO reqVO);
}
