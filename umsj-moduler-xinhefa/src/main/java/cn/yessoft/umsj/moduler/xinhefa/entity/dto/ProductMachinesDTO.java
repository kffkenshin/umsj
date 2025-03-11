package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import cn.yessoft.umsj.common.utils.BaseUtils;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMachinesDTO {
  private int defaultNumber;
  private BigDecimal rollLength;
  private MachineParamsDTO firstMachine;
  private List<MachineParamsDTO> secondMachines;

  public MachineParamsDTO getMachineParamsDTO(String machinNo) {
    if (firstMachine != null && firstMachine.getMachineNo().equals(machinNo)) {
      return firstMachine;
    }
    return secondMachines.stream()
        .filter(i -> i.getMachineNo().equals(machinNo))
        .findFirst()
        .orElse(null);
  }

  public Boolean hasFirst() {
    return BaseUtils.isNotEmpty(firstMachine);
  }
}
