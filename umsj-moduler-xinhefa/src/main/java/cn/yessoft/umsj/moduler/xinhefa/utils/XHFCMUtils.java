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
    // 评分: 10 宽度不同且在范围内 20 宽度相同(自拼)  30 宽度不同,数量不在范围内(需要切) 90 不合法(数量不够,已经被拼好等)
    // 第一次循环 处理最合适的
    for (List<CMRequire> req : allWidthCase) {
      int point = score(req, allowDiffLength, minLength);
      if (point == 10) {
        result.add(createOrder(req, false));
        done = false;
      }
    }
    // 第二次循环 处理需要切的
    for (List<CMRequire> req : allWidthCase) {
      int point = score(req, allowDiffLength, minLength);
      if (point == 30 || point == 10) {
        result.add(createOrder(req, true));
        done = false;
      }
    }
    // 第三次循环 处理自拼
    for (List<CMRequire> req : allWidthCase) {
      int point = score(req, allowDiffLength, minLength);
      if (point == 30 || point == 10 || point == 20) {
        result.add(createOrder(req, false));
        done = false;
      }
    }
    // 递归
    arrange1(reqs, minWidth, maxWidth, minLength, allowDiffLength, result, done);
    return result;
  }

  // 创建订单
  private static CMOrder createOrder(List<CMRequire> req, Boolean needSplit) {
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
    if (req.size() == 1) {
      return 10;
    }
    int maxDif = 0; // 最大差距
    Map<Integer, Integer> widthCount = Maps.newHashMap(); // 每种宽度的个数
    for (CMRequire req1 : req) {
      widthCount.put(req1.getWidth(), BaseUtils.objtoint(widthCount.get(req1.getWidth())) + 1);
    }
    for (CMRequire req1 : req) {
      BigDecimal length1 =
          new BigDecimal(req1.getLength())
              .divide(new BigDecimal(widthCount.get(req1.getWidth())), RoundingMode.CEILING);
      if (length1.intValue() < minLength) {
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
    Date start = new Date();
    List<CMRequire> reqs = Lists.newArrayList();
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
    //    addReq(reqs, 895, 19);
    //    addReq(reqs, 855, 23);
    //    addReq(reqs, 715, 15);
    //    addReq(reqs, 695, 51);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 7  end

    // case 8  start
    //    addReq(reqs, 835, 15);
    //    addReq(reqs, 780, 10);
    //    addReq(reqs, 765, 60);
    //    addReq(reqs, 630, 26);
    //    addReq(reqs, 585, 117);
    //    addReq(reqs, 575, 6);
    //    addReq(reqs, 550, 34);
    //    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 8  end

    // case 9  start
    addReq(reqs, 805, 43);
    addReq(reqs, 800, 17);
    addReq(reqs, 680, 7);
    addReq(reqs, 630, 20);
    //    addReq(reqs, 665, 67);
    //    addReq(reqs, 575, 6);
    //    addReq(reqs, 550, 34);
    List<CMOrder> result = arrange1(reqs, 980, 2150, 5, 3, null, null);
    // case 9  end
    System.out.println("拼吹结果");
    System.out.println(result);
    System.out.println("剩余需求");
    System.out.println(reqs);
    Date end = new Date();
    System.out.println("计算耗时" + (end.getTime() - start.getTime()) + "ms");
  }

  private static void addReq(List<CMRequire> reqs, int width, int length) {
    CMRequire req = new CMRequire();
    req.setLength(length);
    req.setWidth(width);
    reqs.add(req);
  }
}
