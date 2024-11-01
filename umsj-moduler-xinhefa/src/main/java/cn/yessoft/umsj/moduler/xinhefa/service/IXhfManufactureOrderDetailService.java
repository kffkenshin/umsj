package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderBatchDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderDetailDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.util.List;

/**
 * 生产订单明细 服务类
 *
 * @author ethan
 * @since 2024-10-23
 */
public interface IXhfManufactureOrderDetailService extends IService<XhfManufactureOrderDetailDO> {

  XhfManufactureOrderDetailDO createDetail(
      Integer w,
      XhfManufactureOrderBatchDO moBatch,
      long l,
      BigDecimal bigDecimal,
      List<SimulateDetailDTO> detail);

  List<XhfManufactureOrderDetailDO> getByHeaderIds(List<Long> headerIds);
}
