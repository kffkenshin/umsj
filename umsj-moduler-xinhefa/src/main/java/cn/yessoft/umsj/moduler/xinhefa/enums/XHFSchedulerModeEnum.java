package cn.yessoft.umsj.moduler.xinhefa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 排场方式 */
@Getter
@AllArgsConstructor
public enum XHFSchedulerModeEnum {
  A("A", "效率优先"),
  B("B", "交期优先"),
  C("C", "最早开始时间");
  private final String modeNo;
  private final String modeName;
}
