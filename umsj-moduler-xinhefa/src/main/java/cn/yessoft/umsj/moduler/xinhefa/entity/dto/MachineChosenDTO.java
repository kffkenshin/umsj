package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

// ** 选机规则 等选机需要用到的参数 缓存

@Getter
@Setter
public class MachineChosenDTO {

  private Map<String, XhfMachineDO> machines;

  private List<XhfMachinePropertyDO> machineProperties;

  private List<XhfProductProcessDO> productProcesses;

  private List<XhfDynamicMachineDeterminantDO> dynamicMachineDeterminants;
}
