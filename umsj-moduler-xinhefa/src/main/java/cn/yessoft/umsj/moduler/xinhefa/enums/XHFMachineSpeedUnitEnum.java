package cn.yessoft.umsj.moduler.xinhefa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 工批状态 */
@Getter
@AllArgsConstructor
public enum XHFMachineSpeedUnitEnum {
  PCS("只/分"),
  LENGTH("米/分"),
  KG("kg/分"),
  ;

  private final String name;
}
