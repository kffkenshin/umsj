package cn.yessoft.umsj.moduler.xinhefa.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** APS工作站 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum XHFNextStationWaitTimeEnum {
  A("吹膜", "下工作站", "ALL", "", "", "", "", 12, 4, 16),
  B("印刷", "下工作站", "ALL", "", "", "", "", 12, 4, 16),
  C("喷码", "下工作站", "ALL", "", "", "", "", 12, 4, 16),
  D("复合", "下工作站", "ALL", "B11", "", "K", "宝洁", 44, 4, 48),
  E("复合", "下工作站", "ALL", "B11", "", "", "", 24, 2, 26),
  F("复合", "下工作站", "ALL", "B12", "", "K", "尤妮佳", 44, 4, 28),
  G("复合", "下工作站", "ALL", "B12", "", "", "", 18, 2, 20),
  H("复合", "下工作站", "ALL", "B13", "", "K", "大王", 44, 4, 48),
  I("复合", "下工作站", "ALL", "B13", "", "K", "和弘", 44, 4, 48),
  J("复合", "下工作站", "ALL", "B13", "", "K", "尤妮佳", 14, 2, 16),
  L("复合", "下工作站", "ALL", "B13", "", "", "", 24, 2, 26),
  N("复合", "下工作站", "ALL", "B14", "", "", "", 18, 4, 20),
  O("复合", "下工作站", "ALL", "", "", "", "", 12, 4, 16),
  P("检码", "下工作站", "ALL", "", "", "", "", 0, 6, 6),
  Q("刻码", "下工作站", "ALL", "", "", "", "", 0, 6, 6),
  R("烫金", "烫金2", "ALL", "", "", "", "", 0, 4, 4),
  S("烫金", "下工作站", "ALL", "G11", "", "", "", 36, 6, 42),
  T("烫金", "下工作站", "ALL", "G12", "", "", "", 24, 6, 30),
  U("制袋线", "制袋机", "大底封袋", "", "", "", "", 0, 2, 2),
  V("制袋线", "制袋机", "大侧封袋", "", "", "", "", 0, 2, 2),
  W("制袋线", "制袋机", "小侧封袋", "", "", "", "", 0, 2, 2),
  X("制袋线", "制袋机", "成型卷料", "", "", "", "", 0, 4, 4),
  Y("制袋线", "制袋机", "点断卷料", "", "", "", "", 0, 6, 6),
  Z("制袋线", "制袋机", "小底封袋", "", "", "", "", 0, 6, 6),
  A1("制袋线", "制袋机", "小底封卷料", "", "", "", "", 0, 6, 6),
  A2("制袋线", "制袋机", "大底封卷料", "", "", "", "", 0, 6, 6),
  A3("制袋线", "制袋机", "其它", "", "", "", "", 0, 6, 6),
  A4("制袋机", "手工全检", "ALL", "", "", "", "", 0, 6, 6),
  ;
  private final String frontWorkStation;
  private final String nextWorkStation;
  private final String itemType;
  private final String option1;
  private final String optionparams1;
  private final String option2;
  private final String optionparams2;
  private final Integer waitTime;
  private final Integer minFactTime;
  private final Integer totelTime;
}
