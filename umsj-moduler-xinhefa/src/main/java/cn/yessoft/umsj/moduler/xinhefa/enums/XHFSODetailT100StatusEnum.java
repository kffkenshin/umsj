package cn.yessoft.umsj.moduler.xinhefa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** T100 订单状态 */
@Getter
@AllArgsConstructor
public enum XHFSODetailT100StatusEnum {
  TOBE_APPROVE("1", "一般"),
  CLOSED("2", "正常结案"),
  LONG_CLOSED("3", "长结"),
  SHORT_CLOSED("4", "短结"),
  HOLD("5", "留置"),
  ;

  private final String no;
  private final String name;
}
