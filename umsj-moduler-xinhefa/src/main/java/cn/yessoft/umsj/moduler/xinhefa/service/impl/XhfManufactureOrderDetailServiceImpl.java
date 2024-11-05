package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderBatchDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderDetailDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfManufactureOrderDetailMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfManufactureOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 生产订单明细 服务实现类
 *
 * @author ethan
 * @since 2024-10-23
 */
@Service("xhfMoDetailService")
public class XhfManufactureOrderDetailServiceImpl
    extends ServiceImpl<XhfManufactureOrderDetailMapper, XhfManufactureOrderDetailDO>
    implements IXhfManufactureOrderDetailService {
  @Resource private XhfManufactureOrderDetailMapper xhfMoDetailMapper;

  @Override
  public XhfManufactureOrderDetailDO createDetail(
      Integer workStation,
      XhfManufactureOrderBatchDO moBatch,
      long parentDetailId,
      BigDecimal leadTime,
      List<SimulateDetailDTO> detail) {
    XhfManufactureOrderDetailDO detailDO = new XhfManufactureOrderDetailDO();
    detailDO.setBatchId(moBatch.getId());
    detailDO.setWorkStation(workStation);
    detailDO.setHeaderId(moBatch.getHeaderId());
    detailDO.setLeadTime(leadTime);
    // 准备米数
    BigDecimal prepareQty2 = new BigDecimal(0);
    // 过程废品
    BigDecimal processRejectRate = new BigDecimal(0);
    // 异常废品
    BigDecimal actAbnormalRejectRate = new BigDecimal(0);
    // 投入米数
    BigDecimal inputQty2 = new BigDecimal(0);
    // 产出米数
    BigDecimal outputQty = new BigDecimal(0);
    for (SimulateDetailDTO i : detail) {
      if (i.getWorkStation().equals(workStation)) {
        prepareQty2 = prepareQty2.add(i.getPrepareQty2());
        processRejectRate = processRejectRate.add(i.getProcessRejectRate());
        actAbnormalRejectRate = actAbnormalRejectRate.add(i.getActAbnormalRejectRate());
        if (actAbnormalRejectRate.compareTo(new BigDecimal(0)) == 0) {
          inputQty2 = inputQty2.add(i.getInputQty2());
        }
        outputQty = i.getOutputQty();
      }
    }
    detailDO.setInputQty(inputQty2);
    detailDO.setPrepareRejectQty(prepareQty2);
    detailDO.setProcessRejectRate(processRejectRate);
    detailDO.setAbnormalRejectRate(actAbnormalRejectRate);
    detailDO.setOutputQty(outputQty);
    detailDO.setParentDetailId(parentDetailId);
    xhfMoDetailMapper.insert(detailDO);
    return detailDO;
  }

  @Override
  public List<XhfManufactureOrderDetailDO> getByHeaderIds(List<Long> headerIds) {
    return xhfMoDetailMapper.getByHeaderIds(headerIds);
  }

  @Override
  public String executeScheduler() {
    StringBuilder resultMsg = new StringBuilder();
    return resultMsg.toString();
  }
}
