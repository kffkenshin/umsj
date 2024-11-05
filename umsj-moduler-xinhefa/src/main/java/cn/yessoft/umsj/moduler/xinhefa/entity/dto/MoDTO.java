package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderBatchDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderDetailDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderHeaderDO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoDTO {
  private XhfManufactureOrderHeaderDO header;
  private List<XhfManufactureOrderDetailDO> details;
  private List<XhfManufactureOrderBatchDO> batchs;
  private String msg;
}
