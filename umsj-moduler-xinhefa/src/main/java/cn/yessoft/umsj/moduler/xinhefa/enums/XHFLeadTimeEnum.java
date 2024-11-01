package cn.yessoft.umsj.moduler.xinhefa.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** APS生产提前期 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum XHFLeadTimeEnum {
  A("印刷", "ALL", new BigDecimal(1.0), "工艺路线含\"凹印\"或\"柔印\"，无印刷0天"),
  B("喷码", "ALL", new BigDecimal(1.0), "工艺路线含\"喷码\"，无喷码0天"),
  C("复合", "ALL", new BigDecimal(2.0), "工艺路线含\"复合\"，无复合0天"),
  D("检码", "ALL", new BigDecimal(1.0), "工艺路线含\"检码\"或\"双检\"，无0天"),
  E("烫金", "ALL", new BigDecimal(2.0), "工工艺路线含\"烫金\"或\"打电晕\"，无0天"),
  F("刻码", "ALL", new BigDecimal(0.5), "工艺路线含\"刻码\"，无喷码0天"),
  G("制袋线", "ALL", new BigDecimal(0.5), "产品类型是分切卷料的制袋线LT=0天，其它0.5天"),
  H("制袋机", "分切卷料", new BigDecimal(0.5), "按产品类型"),
  I("制袋机", "成型卷料", new BigDecimal(0.5), "按产品类型"),
  J("制袋机", "点断卷料", new BigDecimal(1.0), "按产品类型"),
  K("制袋机", "小底封卷料", new BigDecimal(1.0), "按产品类型"),
  L("制袋机", "大底封卷料", new BigDecimal(1.0), "按产品类型"),
  M("制袋机", "大底封袋", new BigDecimal(1.0), "按产品类型"),
  N("制袋机", "大侧封袋", new BigDecimal(1.5), "按产品类型"),
  O("制袋机", "小侧封袋", new BigDecimal(1.5), "按产品类型"),
  P("制袋机", "小底封袋", new BigDecimal(1.5), "按产品类型"),
  Q("制袋机", "其他", new BigDecimal(1.5), "按产品类型"),
  R("手工全检", "ALL", new BigDecimal(1.0), "工艺路线含\"手工全检\"1天，无\"手工全检\"0天"),
  S("入库", "ALL", new BigDecimal(1.0), "所有产品入库LT=1天"),
  ;
  private final String workStationStr;
  private final String itemType;
  private final BigDecimal ltTime;
  private final String description;

  public static Map<Integer, BigDecimal> getLeadTimeSum(
      Set<Integer> allWorkStation, String itemType) {
    BigDecimal leadTimeSum = S.getLtTime();
    Map<Integer, BigDecimal> result = new HashMap<>();
    // 从后往前算
    List<Integer> list = new ArrayList<>(allWorkStation);
    Collections.reverse(list);
    for (int i = 0; i < list.size(); i++) {
      leadTimeSum = leadTimeSum.add(getLeadTime(list.get(i), itemType).ltTime);
      result.put(list.get(i), new BigDecimal(leadTimeSum.doubleValue()));
    }
    return result;
  }

  public static XHFLeadTimeEnum getLeadTime(Integer workStation, String itemType) {
    if (XHFWorkStationEnum.ZDJ.getCode() == workStation) {
      try {
        return Arrays.stream(XHFLeadTimeEnum.values())
            .filter(
                i ->
                    XHFWorkStationEnum.ZDJ.getName().equals(i.getWorkStationStr())
                        && i.getItemType().equals(itemType))
            .findFirst()
            .get();
      } catch (Exception e) {
        return XHFLeadTimeEnum.Q;
      }
    } else {
      return Arrays.stream(XHFLeadTimeEnum.values())
          .filter(i -> i.getWorkStationStr() == XHFWorkStationEnum.valueOf(workStation).getName())
          .findFirst()
          .get();
    }
  }
}
