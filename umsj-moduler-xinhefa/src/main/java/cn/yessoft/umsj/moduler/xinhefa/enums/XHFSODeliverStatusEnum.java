package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.yessoft.umsj.common.pojo.EnumSelector;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** APS 订单批次状态 */
@Getter
@AllArgsConstructor
public enum XHFSODeliverStatusEnum {
  TOBE_APPROVE("1", "已审核"),
  TOBE_SCHEDULED("2", "待预排"),
  TOBE_LOCK("3", "待锁排"),
  TOBE_DELIVERY("4", "待出货"),
  UNDERSHIPMENT("5", "出货不足"),
  OVERLOADING("6", "出货超发"),
  CLOSED("7", "出货完成"),
  ;

  private final String no;
  private final String name;

  public static XHFSODeliverStatusEnum valueOfNo(String no) {
    return ArrayUtil.firstMatch(e -> e.getNo().equals(no), XHFSODeliverStatusEnum.values());
  }

  public static List<EnumSelector> getSelector() {
    List<EnumSelector> selectors = new ArrayList<EnumSelector>();
    for (XHFSODeliverStatusEnum e : values()) {
      selectors.add(new EnumSelector(e.getNo(), e.getName()));
    }
    return selectors;
  }
}
