package cn.yessoft.umsj.moduler.xinhefa.utils;

import cn.yessoft.umsj.common.utils.BaseUtils;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

public class XHFCMUtils {
  @Data
  public static class CMRequire {
    private Integer width;
    private Integer length;
  }

  @Data
  public static class CMOrder {
    private List<Integer> channel;
    //    private Integer channel1;
    //    private Integer channel2;
    //    private Integer channel3;
    //    private Integer channel4;
    //    private Integer channel5;

    private Integer length;
  }

  // 拼吹 切断优先
  public static List<CMOrder> arrange1(
      List<CMRequire> reqs,
      Integer minWidth,
      Integer maxWidth,
      Integer minLength,
      Integer allowDiffLength,
      List<CMOrder> result,
      Boolean done) {
    if (BaseUtils.isNotEmpty(done) && done) return result;
    done = true;
    if (result == null) result = Lists.newArrayList();
    List<List<CMRequire>> allWidthCase = Lists.newArrayList();
    Comparator<CMRequire> comparator = Comparator.comparing(CMRequire::getWidth);
    findAllCombination(
        reqs,
        maxWidth,
        new ArrayList<>(),
        allWidthCase,
        0,
        reqs.stream().min(comparator).get().getWidth());
    // 1214 有可能出现 漏掉的自拼组合 补充 比如 2100 下 785 和575 会漏掉785 785 组合
    checkSelfCombination(reqs, allWidthCase, maxWidth);
    // 评分: 10 宽度不同且在范围内 20 宽度相同(自拼)  30 宽度不同,数量不在范围内(需要切) 90 不合法(数量不够,已经被拼好等)
    // 第一次循环 处理最合适的
    for (List<CMRequire> req : allWidthCase) {
      int point = score(req, allowDiffLength, minLength);
      if (point == 10) {
        result.add(createOrder(req, false, minLength));
        done = false;
      }
    }
    // 第二次循环 处理需要切的
    for (List<CMRequire> req : allWidthCase) {
      int point = score(req, allowDiffLength, minLength);
      if (point == 30 || point == 10) {
        result.add(createOrder(req, true, minLength));
        done = false;
      }
    }
    // 第三次循环 处理自拼
    for (List<CMRequire> req : allWidthCase) {
      int point = score(req, allowDiffLength, minLength);
      if (point == 30 || point == 10 || point == 20) {
        result.add(createOrder(req, false, minLength));
        done = false;
      }
    }
    // 递归
    arrange1(reqs, minWidth, maxWidth, minLength, allowDiffLength, result, done);
    return result;
  }

  // 补充漏掉的自拼组合
  private static void checkSelfCombination(
      List<CMRequire> reqs, List<List<CMRequire>> allWidthCase, Integer maxWidth) {
    reqs.forEach(
        req -> {
          if (!allWidthCase.stream()
              .anyMatch(
                  i -> {
                    for (CMRequire cmRequire : i) {
                      if (cmRequire.getWidth() != req.getWidth()) {
                        return false;
                      }
                    }
                    return true;
                  })) {
            allWidthCase.add(createSelfCombination(req, maxWidth));
          }
        });
  }

  private static List<CMRequire> createSelfCombination(CMRequire req, Integer maxWidth) {
    List<CMRequire> result = Lists.newArrayList();
    Integer tWidth = 0;
    while (tWidth + req.getWidth() < maxWidth) {
      result.add(req);
      tWidth += req.getWidth();
    }
    return result;
  }

  // 创建订单  1214 增加最小起拼量参数
  private static CMOrder createOrder(List<CMRequire> req, Boolean needSplit, Integer minLength) {
    CMOrder order = new CMOrder();
    List<Integer> channel = Lists.newArrayList();
    Map<Integer, Integer> widthCount = Maps.newHashMap(); // 每种宽度的个数
    Integer max = 0;
    Integer min = 99999;
    for (CMRequire req1 : req) {
      widthCount.put(req1.getWidth(), BaseUtils.objtoint(widthCount.get(req1.getWidth())) + 1);
    }
    for (CMRequire req1 : req) {
      BigDecimal length1 =
          new BigDecimal(req1.getLength())
              .divide(new BigDecimal(widthCount.get(req1.getWidth())), RoundingMode.CEILING);
      if (min > length1.intValue()) {
        min = length1.intValue();
      }
      if (max < length1.intValue()) {
        max = length1.intValue();
      }
    }
    // 1214
    if (min < minLength) {
      min = minLength;
    }
    if (max < minLength) {
      max = minLength;
    }
    if (needSplit) {
      for (CMRequire i : req) {
        channel.add(i.getWidth());
        i.setLength(i.getLength() - min);
      }
      order.setLength(min);
    } else {
      req.forEach(
          i -> {
            channel.add(i.getWidth());
            i.setLength(0);
          });
      order.setLength(max);
    }
    order.setChannel(channel);
    return order;
  }

  // 评分: 10 宽度不同且在范围内 20 宽度相同(自拼)  30 宽度不同,数量不在范围内(需要切) 90 不合法(数量不够,已经被拼好等)
  private static int score(List<CMRequire> req, Integer allowDiffLength, Integer minLength) {

    // 1227 下调自拼优先级
    if (req.size() == 1 && req.get(0).getLength() > 0) {
      return 20;
    }
    // 1227 加入最小幅宽判断
    if (req.size() == 1 && req.get(0).getLength() < minLength) {
      return 90;
    }
    int maxDif = 0; // 最大差距
    Map<Integer, Integer> widthCount = Maps.newHashMap(); // 每种宽度的个数
    Boolean allZero = true;
    for (CMRequire req1 : req) {
      widthCount.put(req1.getWidth(), BaseUtils.objtoint(widthCount.get(req1.getWidth())) + 1);
      if (req1.getLength() > 0) allZero = false;
    }
    // 1214 修改 全部为0 返回90
    if (allZero) return 90;
    for (CMRequire req1 : req) {
      BigDecimal length1 =
          new BigDecimal(req1.getLength())
              .divide(new BigDecimal(widthCount.get(req1.getWidth())), RoundingMode.CEILING);
      // 1214 修改  起拼量-差异数
      if (length1.intValue() < minLength - allowDiffLength) {
        return 90;
      }
      for (CMRequire req2 : req) {
        BigDecimal length2 =
            new BigDecimal(req2.getLength())
                .divide(new BigDecimal(widthCount.get(req2.getWidth())), RoundingMode.CEILING);
        maxDif = Math.max(maxDif, Math.abs(length1.intValue() - length2.intValue()));
      }
    }
    if (widthCount.size() == 1) {
      return 20;
    } else {
      if (maxDif <= allowDiffLength) {
        return 10;
      } else {
        return 30;
      }
    }
  }

  // 找出所有组合
  private static void findAllCombination(
      List<CMRequire> reqs,
      int remaining,
      List<CMRequire> combination,
      List<List<CMRequire>> result,
      int start,
      int minWidth) {
    // 如果剩余小于0，说明该组合不成立
    if (remaining < 0) {
      return;
    }
    // 如果剩余为0，说明找到了一个组合
    if (remaining < minWidth) {
      result.add(new ArrayList<>(combination));
      return;
    }
    // 遍历所有宽度，从当前宽度开始
    for (int i = start; i < reqs.size(); i++) {
      combination.add(reqs.get(i));
      // 递归调用
      findAllCombination(
          reqs,
          remaining - reqs.get(i).getWidth(),
          combination,
          result,
          i,
          minWidth); // 允许重复使用同一需求宽度
      // 撤回选择，准备下一个组合
      combination.remove(combination.size() - 1);
    }
  }

  public static void main(String[] args) {
    //    Date start = new Date();
    //    List<CMRequire> reqs = Lists.newArrayList();
    // case 1  start
    //    addReq(reqs, 1010, 5);
    //    addReq(reqs, 755, 46);
    //    addReq(reqs, 720, 3);
    //    addReq(reqs, 685, 19);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 1  end

    // case 2  兰兰黑板start
    //    addReq(reqs, 1005, 14);
    //    addReq(reqs, 925, 8);
    //    addReq(reqs, 840, 7);
    //    addReq(reqs, 830, 13);
    //    addReq(reqs, 785, 18);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 2  end

    // case 3  start
    //    addReq(reqs, 692, 17);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 3  end

    // case 4  start
    //    addReq(reqs, 1010, 6);
    //    addReq(reqs, 755, 26);
    //    addReq(reqs, 720, 5);
    //    addReq(reqs, 685, 40);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 4  end

    //     case 5  start
    //    addReq(reqs, 1090, 5);
    //    addReq(reqs, 1015, 9);
    //    addReq(reqs, 840, 15);
    //    addReq(reqs, 830, 16);
    //    addReq(reqs, 785, 77);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    //     case 5  end

    // case 6  start
    //    addReq(reqs, 750, 5);
    //    addReq(reqs, 720, 39);
    //    addReq(reqs, 715, 37);
    //    addReq(reqs, 700, 5);
    //    addReq(reqs, 555, 117);
    //    List<CMOrder> result = arrange1(reqs, 980, 1600, 5, 3, null, null);
    // case 6  end

    // case 7  start
    //        addReq(reqs, 895, 19);
    //        addReq(reqs, 855, 23);
    //        addReq(reqs, 715, 15);
    //        addReq(reqs, 695, 51);
    //        List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 7  end

    // case 8  start
    //    addReq(reqs, 1312, 30);
    //    addReq(reqs, 890, 4);
    //    addReq(reqs, 870, 17);
    //    addReq(reqs, 750, 8);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 8  end

    // case 9  start
    //    addReq(reqs, 1248, 22);
    //    addReq(reqs, 870, 45);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 9  end
    //    System.out.println("拼吹结果");
    //    System.out.println(result);
    //    System.out.println("剩余需求");
    //    System.out.println(reqs);
    //    Date end = new Date();
    //    System.out.println("计算耗时" + (end.getTime() - start.getTime()) + "ms");
    String[] input = {
      "A", "B", "C", "A", "B", "A", "A", "B", "A", "B", "B", "B", "B", "A", "B", "B", "C", "B", "B",
      "B", "B", "A", "B", "B", "B", "B", "A", "B", "B", "C", "A", "A", "B", "C", "B", "B", "B", "B",
      "B", "B", "B"
    };
    System.out.println(extractFeatures(input));
  }

  private static void addReq(List<CMRequire> reqs, int width, int length) {
    CMRequire req = new CMRequire();
    req.setLength(length);
    req.setWidth(width);
    reqs.add(req);
  }

  public static class MoSoRelation {
    private String moBatchUid;
    private String soDeliverUid;
    private double qty;
  }

  //  public static void initAllacate(String 单号项次) {
  //    clearByOrder(单号项次);
  //    List<SoDeliver> so = getEffectiveSoDeliver(单号项次);
  //    List<MoBatch> mo = getEffectiveMoBatch(单号项次);
  //    for (SoDeliver e : so) {
  //      for (MoBatch eMo : mo) {
  //        createRelation(soUid, moUid, allCateQty);
  //      }
  //    }
  //    // todo 处理 mo 没匹配到的
  //    // todo 处理 so 没匹配到的
  //  }
  //  // 更新工单批的需求日期  传入 xxxx
  //  updateMoBacthRequireDate(req)
  //
  //    getEarlierReqireDateByMoBacthUid(工单批)

  public static List<String> extractFeatures(String[] inputArray) {
    List<String> features = new ArrayList<>();
    StringBuilder temp = new StringBuilder(); // 临时寄存,不用String。因为StringBuilder性能比String高
    int len = inputArray.length;
    String feature = ""; // 寄存特征。

    for (int i = 0; i <= len; i++) {

      feature = i < len ? inputArray[i] : ""; // 用于解决最后一次匹配所以for中i<=len
      if ("ABC".contentEquals(temp)
          || ("AB".contentEquals(temp) && !"C".equals(feature))
          || (temp.toString().startsWith("BBB") && !"B".equals(feature))) { // 按规则提取出栈
        features.add(temp.toString());
        temp.setLength(0);
      }

      if (("A".equals(feature) && temp.length() < 1)
          || "B".equals(feature)
          || ("C".equals(feature) && "AB".contentEquals(temp))) { // 按特征拼接
        temp.append(feature);
      } else { // 不符合清容
        temp.setLength(0);
        //        temp.append(feature.equals("C") ? "" : feature); // 不能以C开头
      }
    }
    return features;
  }
}
