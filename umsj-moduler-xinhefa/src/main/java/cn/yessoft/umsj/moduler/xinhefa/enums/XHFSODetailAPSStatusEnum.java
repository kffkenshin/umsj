package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.yessoft.umsj.common.pojo.EnumSelector;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** APS 订单明细状态 */
@Getter
@AllArgsConstructor
public enum XHFSODetailAPSStatusEnum {
  TOBE_APPROVE("1", "已审核"),
  TOBE_SCHEDULED("2", "待预排"),
  TOBE_LOCK("3", "待锁排"),
  TOBE_DELIVERY("4", "待出货"),
  DELIVERING("5", "出货中"),
  DELIVERED("6", "出货完"),
  CLOSED("7", "已完成"),
  FROZEN("8", "已冻结"),
  HOLD("9", "留置"),
  ;

  private final String no;
  private final String name;

  public static XHFSODetailAPSStatusEnum valueOfNo(String no) {
    return ArrayUtil.firstMatch(e -> e.getNo().equals(no), XHFSODetailAPSStatusEnum.values());
  }

  public static List<EnumSelector> getSelector() {
    List<EnumSelector> selectors = new ArrayList<EnumSelector>();
    for (XHFSODetailAPSStatusEnum e : values()) {
      selectors.add(new EnumSelector(e.getNo(), e.getName()));
    }
    return selectors;
  }
}
