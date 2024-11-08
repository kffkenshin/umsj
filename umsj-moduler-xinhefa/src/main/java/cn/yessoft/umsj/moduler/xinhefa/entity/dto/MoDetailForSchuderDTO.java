package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoDetailForSchuderDTO {

  private XhfManufactureOrderDetailDO moDetail;

  private XhfItemDO item;

  private XhfManufactureOrderBatchDO moBatch;

  private XhfManufactureOrderHeaderDO moHeader;

  private XhfMachinePropertyDO machineProperty;

  private List<XhfMachineDisablePlanDO> machineDisablePlans;

  private LocalDateTime requireDate;

  public LocalDateTime getRequireDate() {
    return moBatch.getRequireDate().plusDays(-moDetail.getLeadTime().longValue());
  }
}
