package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum XHFSchedulerModeEnum {
  A1("A1", "印刷效率优先(考虑油墨体系)"),
  A2("A2", "印刷效率优先(不考虑油墨体系)"),
  B("B", "需求日期优先"),
  C("C", "最早开始时间"),
  D("D", "复合效率优先");

  private final String modeNo;
  private final String modeName;

  public static XHFSchedulerModeEnum valueof(String value) {
    return ArrayUtil.firstMatch(e -> e.getModeNo().equals(value), XHFSchedulerModeEnum.values());
  }
}
