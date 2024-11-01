package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderBatchDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderHeaderDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFMOBatchStatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfManufactureOrderBatchMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfManufactureOrderBatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 生产订单批次 服务实现类
 *
 * @author ethan
 * @since 2024-10-23
 */
@Service("xhfMoBatchService")
public class XhfManufactureOrderBatchServiceImpl
    extends ServiceImpl<XhfManufactureOrderBatchMapper, XhfManufactureOrderBatchDO>
    implements IXhfManufactureOrderBatchService {

  @Resource private XhfManufactureOrderBatchMapper xhfManufactureOrderBatchMapper;

  @Override
  public XhfManufactureOrderBatchDO createHeaderBatch(
      XhfManufactureOrderHeaderDO moHeader, LocalDateTime requeriedDate) {
    XhfManufactureOrderBatchDO moBatch = new XhfManufactureOrderBatchDO();
    moBatch.setHeaderId(moHeader.getId());
    moBatch.setBatchNo(1);
    moBatch.setBatchQty(moHeader.getQty());
    moBatch.setRequireDate(requeriedDate);
    moBatch.setStatus(XHFMOBatchStatusEnum.TOBE_PLAN.getNo());
    this.save(moBatch);
    return moBatch;
  }

  @Override
  public List<XhfManufactureOrderBatchDO> getByHeaderIds(List<Long> headerIds) {
    return xhfManufactureOrderBatchMapper.getByHeaderIds(headerIds);
  }
}
