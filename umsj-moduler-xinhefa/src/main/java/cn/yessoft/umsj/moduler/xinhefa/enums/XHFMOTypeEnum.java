package cn.yessoft.umsj.moduler.xinhefa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 工单类型 */
@Getter
@AllArgsConstructor
public enum XHFMOTypeEnum {
  P1("P1", "产品订单工单"),
  P2("P2", "产品手开工单"),
  M1("M1", "薄膜工单"),
  ;

  private final String no;
  private final String name;
}
