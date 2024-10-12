package cn.yessoft.umsj.moduler.xinhefa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** T100 订单状态 */
@Getter
@AllArgsConstructor
public enum XHFSOHeaderStatusEnum {
  TOBE_APPROVE("A", "已核准"),
  CLOSED("C", "结案"),
  DRAFT("N", "未审核"),
  CANCEL("X", "已作废"),
  HOLD("H", "留置"),
  UN_HOLD("UH", "取消留置"),
  ;

  private final String no;
  private final String name;
}
