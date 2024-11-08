package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** 工单明细状态 */
@Getter
@AllArgsConstructor
public enum XHFMODetailStatusEnum {
  FINISHED(10, "已完成"),
  LOCKED(20, "已锁排"),
  TOBE_LOCKED(30, "待锁排"),
  ;

  private final Integer no;
  private final String name;

  public static XHFMODetailStatusEnum valueOf(Integer no) {
    return ArrayUtil.firstMatch(e -> e.getNo().equals(no), XHFMODetailStatusEnum.values());
  }
}
