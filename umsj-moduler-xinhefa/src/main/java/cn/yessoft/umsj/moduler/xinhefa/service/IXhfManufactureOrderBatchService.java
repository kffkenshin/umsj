package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderBatchDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderHeaderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 生产订单批次 服务类
 *
 * @author ethan
 * @since 2024-10-23
 */
public interface IXhfManufactureOrderBatchService extends IService<XhfManufactureOrderBatchDO> {

  XhfManufactureOrderBatchDO createHeaderBatch(
      XhfManufactureOrderHeaderDO moHeader, LocalDateTime requeriedDate);

    List<XhfManufactureOrderBatchDO> getByHeaderIds(List<Long> headerIds);
}
