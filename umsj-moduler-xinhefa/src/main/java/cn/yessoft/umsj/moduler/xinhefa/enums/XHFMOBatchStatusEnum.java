package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** 工批状态 */
@Getter
@AllArgsConstructor
public enum XHFMOBatchStatusEnum {
  TOBE_PLAN(10, "待预排"),
  TOBE_LOCK(20, "待锁排"),
  TOBE_YS(30, "待印刷"),
  TOBE_PM(40, "待喷码"),
  TOBE_FH(50, "待复合"),
  TOBE_JM(60, "待检码"),
  TOBE_TJ(70, "待烫金"),
  TOBE_KM(80, "待刻码"),
  TOBE_ZX(90, "待制线"),
  TOBE_ZJ(100, "待制机"),
  TOBE_RK(110, "待入库"),
  FINISHED(120, "已完成"),
  CANCEL(999, "已作废"),
  ;

  private final Integer no;
  private final String name;

  public static XHFMOBatchStatusEnum valueOf(Integer no) {
    return ArrayUtil.firstMatch(e -> e.getNo().equals(no), XHFMOBatchStatusEnum.values());
  }
}
