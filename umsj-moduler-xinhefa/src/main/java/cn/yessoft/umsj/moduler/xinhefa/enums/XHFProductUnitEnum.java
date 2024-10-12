package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** APS系统固定角色 */
@Getter
@AllArgsConstructor
public enum XHFProductUnitEnum {
  KG("千克"), // 千克
  KM("千米"), // 千米
  M("米"), // 米
  PCS("只"), // 只
  KPCS("千只"), // 千只
  WPCS("万只"), // 万只
  ;

  private final String unit;

  public static XHFProductUnitEnum valueof(String value) {
    return ArrayUtil.firstMatch(e -> e.getUnit().equals(value), XHFProductUnitEnum.values());
  }
}
