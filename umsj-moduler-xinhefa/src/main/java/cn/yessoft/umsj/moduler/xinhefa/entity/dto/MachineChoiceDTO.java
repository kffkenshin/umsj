package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineChoiceDTO {
  private Long itemId;
  private Long customerId;

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MachineChoiceDTO machineChoiceDTO) {
      return Objects.equals(machineChoiceDTO.getCustomerId(), this.customerId)
          && Objects.equals(machineChoiceDTO.getItemId(), this.itemId);
    }
    return false;
  }
}
