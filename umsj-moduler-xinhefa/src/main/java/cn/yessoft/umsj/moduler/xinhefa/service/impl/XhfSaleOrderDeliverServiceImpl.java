package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;
import static cn.yessoft.umsj.moduler.xinhefa.enums.XHFSODeliverStatusEnum.TOBE_APPROVE;

import cn.hutool.core.date.DateUtil;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderDeliverCreateBatchInfoVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderDeliverCreateVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSaleOrderDeliverDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSaleOrderDetailDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSaleOrderHeaderDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSODetailAPSStatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfSaleOrderDeliverMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSaleOrderDeliverService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSaleOrderDetailService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSaleOrderHeaderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单批次 服务实现类
 *
 * @author ethan
 * @since 2024-10-15
 */
@Service("xhfSaleOrderDeliverService")
public class XhfSaleOrderDeliverServiceImpl
    extends ServiceImpl<XhfSaleOrderDeliverMapper, XhfSaleOrderDeliverDO>
    implements IXhfSaleOrderDeliverService {

  @Resource private XhfSaleOrderDeliverMapper xhfSaleOrderDeliverMapper;
  @Resource private IXhfSaleOrderDetailService xhfSaleOrderDetailService;
  @Resource private IXhfSaleOrderHeaderService xhfSaleOrderHeaderService;
  @Resource private IXhfCustomerService xhfCustomerService;

  @Override
  @Transactional
  public void batchCreate(List<SaleOrderDeliverCreateVO> reqVO) {
    reqVO.forEach(
        i -> {
          XhfSaleOrderDetailDO detail = xhfSaleOrderDetailService.getById(i.getId());
          if (detail == null) {
            throw exception(O_NOT_EXISTS, "订单");
          }
          if (BaseUtils.isEmpty(i.getDeleverDetail())) {
            throw exception(O_NOT_EXISTS, "订单批次");
          }
          XhfSaleOrderHeaderDO header = xhfSaleOrderHeaderService.getById(detail.getSoId());
          XhfCustomerDO customer = xhfCustomerService.getById(header.getCustomerId());
          int batchNo = 1;
          for (SaleOrderDeliverCreateBatchInfoVO k : i.getDeleverDetail()) {
            XhfSaleOrderDeliverDO entity = new XhfSaleOrderDeliverDO();
            entity.setBatchNo(batchNo++);
            entity.setDetailId(detail.getId());
            entity.setDeliverDate(DateUtil.parseLocalDateTime(k.getDate(), "yyyy-MM-dd"));
            entity.setHeaderId(detail.getSoId());
            entity.setQty(k.getQty());
            entity.setStatus(TOBE_APPROVE.getNo());
            // 物流天数
            Integer ld = customer.getLogisticsDays() == null ? 1 : customer.getLogisticsDays();
            // 入库提前期
            Integer wld =
                customer.getWarehousingLeadDays() == null ? 1 : customer.getWarehousingLeadDays();
            entity.setLogisticsDays(ld);
            entity.setWarehousingLeadDays(wld);
            xhfSaleOrderDeliverMapper.insert(entity);
          }
          detail.setApsStatus(XHFSODetailAPSStatusEnum.TOBE_SCHEDULED.getNo());
          xhfSaleOrderDetailService.updateById(detail);
        });
  }
}
