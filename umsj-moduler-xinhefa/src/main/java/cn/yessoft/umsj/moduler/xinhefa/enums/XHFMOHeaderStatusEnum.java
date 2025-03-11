package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.yessoft.umsj.common.pojo.EnumSelector;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** 工单头状态 */
@Getter
@AllArgsConstructor
public enum XHFMOHeaderStatusEnum {
  TOBE_PLANED("30", "待预排"),
  ERROR1("91", "主数据异常"),
  ERROR2("92", "订单变更");

  private final String no;
  private final String name;

  public static List<EnumSelector> getSelector() {
    List<EnumSelector> selectors = new ArrayList<EnumSelector>();
    for (XHFMOHeaderStatusEnum e : values()) {
      selectors.add(new EnumSelector(e.getNo(), e.getName()));
    }
    return selectors;
  }

  public static XHFMOHeaderStatusEnum valueof(String no) {
    return ArrayUtil.firstMatch(e -> e.getNo().equals(no), XHFMOHeaderStatusEnum.values());
  }
}
