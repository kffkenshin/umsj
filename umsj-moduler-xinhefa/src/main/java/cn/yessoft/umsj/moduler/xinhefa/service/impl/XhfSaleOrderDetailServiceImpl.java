package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSaleOrderDetailDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfSaleOrderDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfSaleOrderDetailMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSaleOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 销售订单明细表 服务实现类
 *
 * @author ethan
 * @since 2024-10-03
 */
@Service("xhfSaleOrderDetailService")
public class XhfSaleOrderDetailServiceImpl
    extends ServiceImpl<XhfSaleOrderDetailMapper, XhfSaleOrderDetailDO>
    implements IXhfSaleOrderDetailService {
  @Resource private XhfSaleOrderDetailMapper xhfSaleOrderDetailMapper;

  @Override
  public XhfSaleOrderDetailDO getBySOIDAndSeq(Long id, Integer detailSeq) {
    return xhfSaleOrderDetailMapper.selectOne(
        XhfSaleOrderDetailDO::getSoId, id, XhfSaleOrderDetailDO::getDetailSeq, detailSeq);
  }

  @Override
  public PageResult<XhfSaleOrderDetailDTO> pagedQuery(SaleOrderQueryReqVO reqVO) {
    return xhfSaleOrderDetailMapper.pagedQuery(reqVO);
  }
}
