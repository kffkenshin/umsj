package cn.yessoft.umsj.moduler.xinhefa.utils;

import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductBomDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFManufactureRuleEnum;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MachineRuleUtil {
  public static boolean isFit(
      Integer workStation,
      XhfDynamicMachineDeterminantDO e,
      XhfItemDO item,
      List<XhfProductBomDTO> bom,
      String xdxNo,
      List<SimulateDetailDTO> routes,
      XhfCustomerDO customer) {
    // 判断工作站
    if (BaseUtils.isNotEmpty(e.getWorkStation()) && !e.getWorkStation().equals(workStation)) {
      return false;
    }
    // 判断产品类型
    if (BaseUtils.isNotEmpty(e.getItemType())
        && !e.getItemType().equals("ALL")
        && !e.getItemType().equals(item.getItemType1())) {
      return false;
    }
    // 判断客户
    if (BaseUtils.isNotEmpty(e.getCustomerName()) && !"其他客户".equals(e.getCustomerName())) {
      if (e.getCustomerName().equals("金佰利")
          && customer.getNameShort().contains("金佰利")
          && !(customer.getNameShort().contains("中国")
              || customer.getNameShort().contains("北京")
              || customer.getNameShort().contains("南京")
              || customer.getNameShort().contains("天津"))) {
        return false;
      } else if (!customer.getNameShort().contains(e.getCustomerName())) {
        return false;
      }
    }
    // 判断制袋机产线
    if (BaseUtils.isNotEmpty(e.getMachineNo()) && !e.getMachineNo().equals(xdxNo)) {
      return false;
    }
    if (BaseUtils.isNotEmpty(e.getOptionCode3())) {
      return isFit(e.getOptionCode1(), e.getOptionParams1(), item, bom, routes, customer)
          && isFit(e.getOptionCode2(), e.getOptionParams2(), item, bom, routes, customer)
          && isFit(e.getOptionCode3(), e.getOptionParams3(), item, bom, routes, customer);
    }
    if (BaseUtils.isNotEmpty(e.getOptionCode2())) {
      return isFit(e.getOptionCode1(), e.getOptionParams1(), item, bom, routes, customer)
          && isFit(e.getOptionCode2(), e.getOptionParams2(), item, bom, routes, customer);
    }
    return isFit(e.getOptionCode1(), e.getOptionParams1(), item, bom, routes, customer);
  }

  public static boolean isFit(
      String machineNo,
      XhfItemDO item,
      List<XhfProductBomDTO> bom,
      XhfCustomerDO customer,
      List<SimulateDetailDTO> routes,
      XhfMachinePropertyDO e,
      Map<String, XhfMachineDO> machines) {
    if (machines.get(machineNo) == null) {
      return false;
    }
    if (!machines.get(machineNo).getId().equals(e.getMachineId())) {
      return false;
    }
    if (BaseUtils.isNotEmpty(e.getRuleOption2())) {
      return isFit(e.getRuleOption2(), e.getRuleOptionContent2(), item, bom, routes, customer)
          && isFit(e.getRuleOption1(), e.getRuleOptionContent1(), item, bom, routes, customer);
    }
    return isFit(e.getRuleOption1(), e.getRuleOptionContent1(), item, bom, routes, customer);
  }

  private static boolean isFit(
      String optionCode,
      String optionParams,
      XhfItemDO item,
      List<XhfProductBomDTO> bom,
      List<SimulateDetailDTO> routes,
      XhfCustomerDO customer) {
    if (BaseUtils.isEmpty(optionCode)) {
      return true;
    }
    switch (XHFManufactureRuleEnum.valueOfOptionCode(optionCode)) {
      case K1 -> {
        if (BaseUtils.isEmpty(optionParams)) return true;
        if (optionParams.equals("金佰利")) {
          if (customer.getNameShort().contains(optionParams)
              && (customer.getNameShort().contains("中国")
                  || customer.getNameShort().contains("北京")
                  || customer.getNameShort().contains("南京")
                  || customer.getNameShort().contains("天津"))) {
            return true;
          }
        } else {
          return customer.getNameShort().contains(optionParams);
        }
      }
      case C11 -> {
        return routes.stream().anyMatch(i -> i.getProcessName().equals("烫封贴标"));
      }
      case C12 -> {
        return routes.stream().noneMatch(i -> i.getProcessName().equals("烫封贴标"));
      }
      case FQ1, FQ2, FQ3, FQ4, FQ5, FQ6 -> {
        return item.getCategoryName()
            .contains(XHFManufactureRuleEnum.valueOfOptionCode(optionCode).getOptionName());
      }
      case FQ7, DDF2, B15, B25 -> {
        return true;
      }
      case DDF1 -> {
        return item.getCategoryName().contains("尿裤");
      }
      case GK1 -> {
        return holeDiff(item.getItemSpec()) == 20;
      }
      case GK2 -> {
        return holeDiff(item.getItemSpec()) == 40;
      }
      case G11 -> {
        return routes.stream().anyMatch(i -> i.getProcessName().equals("无溶剂复合"));
      }
      case G12 -> {
        return routes.stream().noneMatch(i -> i.getProcessName().equals("无溶剂复合"));
      }
      case G21 -> {
        return item.getProcessRemark().contains("烫金") && item.getProcessRemark().contains("打电晕");
      }
      case G22 -> {
        return item.getProcessRemark().contains("烫金") && !item.getProcessRemark().contains("打电晕");
      }
      case G23 -> {
        return !item.getProcessRemark().contains("烫金") && item.getProcessRemark().contains("打电晕");
      }
      case G24 -> {
        return item.getProcessRemark().contains("烫金2");
      }
      case G31 -> {
        return item.getProcessRemark().matches(daKongReg);
      }
      case G32 -> {
        return !item.getProcessRemark().matches(daKongReg);
      }
      case G41 -> {
        return routes.stream().anyMatch(i -> i.getProcessName().equals("手工穿绳"));
      }
      case G42 -> {
        return routes.stream().noneMatch(i -> i.getProcessName().equals("手工穿绳"));
      }
      case G51 -> {
        return bom.stream().anyMatch(i -> i.getMaterialNo().startsWith("4040"));
      }
      case G52 -> {
        return bom.stream().noneMatch(i -> i.getMaterialNo().startsWith("4040"));
      }
      case G61 -> {
        return routes.stream().anyMatch(i -> i.getProcessName().equals("凹印"))
            || routes.stream().anyMatch(i -> i.getProcessName().equals("柔印"));
      }
      case G62 -> {
        return routes.stream().noneMatch(i -> i.getProcessName().equals("凹印"))
            && routes.stream().noneMatch(i -> i.getProcessName().equals("柔印"));
      }
      case G71 -> {
        return routes.stream().anyMatch(i -> i.getProcessName().equals("喷码"));
      }
      case G72 -> {
        return routes.stream().anyMatch(i -> i.getProcessName().equals("打撕裂线"));
      }
      case G81 -> {
        return bom.stream().anyMatch(i -> i.getMaterialName().contains("手提条"));
      }
      case G82 -> {
        return bom.stream().noneMatch(i -> i.getMaterialName().contains("手提条"));
      }
      case G91 -> {
        return routes.stream().anyMatch(i -> i.getProcessName().equals("激光刻码"));
      }
      case G92 -> {
        return routes.stream().noneMatch(i -> i.getProcessName().equals("激光刻码"));
      }
      case G101 -> {
        return routes.stream().anyMatch(i -> i.getProcessName().equals("机器双检"));
      }
      case G102 -> {
        return routes.stream().noneMatch(i -> i.getProcessName().equals("机器检码"));
      }
      case B01 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
            .anyMatch(i -> i.getMaterialName().contains("PPE"));
      }
      case B02 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
            .anyMatch(
                i -> i.getMaterialName().contains("PE") && !i.getMaterialName().contains("PPE"));
      }
      case B03 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
            .anyMatch(
                i -> i.getMaterialName().contains("PP") && !i.getMaterialName().contains("PPE"));
      }
      case B04 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
            .anyMatch(
                i -> !i.getMaterialName().contains("PP") && !i.getMaterialName().contains("PE"));
      }
      case B11 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("FH"))
            .anyMatch(i -> i.getMaterialName().contains("金砂膜"));
      }
      case B12 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("FH"))
            .anyMatch(i -> i.getMaterialName().contains("拉丝膜"));
      }
      case B13 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("FH"))
            .anyMatch(
                i ->
                    i.getMaterialName().contains("PE")
                        && !i.getMaterialName().contains("拉丝膜")
                        && !i.getMaterialName().contains("金砂膜")
                        && !i.getMaterialName().contains("磨砂膜"));
      }
      case B14 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("FH"))
            .anyMatch(
                i -> i.getMaterialName().contains("PP") && !i.getMaterialName().contains("PPE"));
      }
      case B21 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
            .filter(i -> i.getMaterialNo().startsWith("2"))
            .anyMatch(i -> i.getMaterialNo().charAt(4) == '0');
      }
      case B22 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
            .filter(i -> i.getMaterialNo().startsWith("2"))
            .anyMatch(i -> i.getMaterialNo().charAt(4) == '1');
      }
      case B23 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
            .filter(i -> i.getMaterialNo().startsWith("2"))
            .anyMatch(i -> i.getMaterialNo().charAt(4) == '2');
      }
      case B24 -> {
        return bom.stream()
            .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
            .filter(i -> i.getMaterialNo().startsWith("2"))
            .anyMatch(i -> i.getMaterialNo().charAt(4) == '3');
      }
      case B31 -> {
        XhfProductBomDTO dto =
            bom.stream()
                .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
                .filter(i -> i.getMaterialNo().startsWith("2"))
                .findFirst()
                .orElse(null);
        if (dto != null) {
          BigDecimal tick = dto.getMaterialTickness();
          return evaluateCondition(optionParams, tick);
        }
        return false;
      }
      case B32 -> {
        XhfProductBomDTO dto =
            bom.stream()
                .filter(i -> i.getProcessNo().equals("FH"))
                .filter(i -> i.getMaterialNo().startsWith("2"))
                .findFirst()
                .orElse(null);
        if (dto != null) {
          BigDecimal tick = dto.getMaterialTickness();
          return evaluateCondition(optionParams, tick);
        }
        return false;
      }
      case B41 -> {
        XhfProductBomDTO dto =
            bom.stream()
                .filter(i -> i.getProcessNo().equals("YS") || i.getProcessNo().equals("RY"))
                .filter(i -> i.getMaterialNo().startsWith("2"))
                .findFirst()
                .orElse(null);
        if (dto != null) {
          return evaluateCondition(optionParams, dto.getMaterialWidth());
        }
        return false;
      }
      case B42 -> {
        XhfProductBomDTO dto =
            bom.stream()
                .filter(i -> i.getProcessNo().equals("FH"))
                .filter(i -> i.getMaterialNo().startsWith("2"))
                .findFirst()
                .orElse(null);
        if (dto != null) {
          return evaluateCondition(optionParams, dto.getMaterialWidth());
        }
        return false;
      }
      case B43 -> {
        XhfProductBomDTO dto =
            bom.stream().filter(i -> i.getMaterialNo().startsWith("2")).findFirst().orElse(null);
        if (dto != null) {
          return evaluateCondition(optionParams, dto.getMaterialWidth());
        }
        return false;
      }
      case D1 -> {
        return evaluateCondition(optionParams, new BigDecimal(item.getLength()));
      }
      case D2 -> {
        return evaluateCondition(optionParams, new BigDecimal(item.getWidth()));
      }
      case D3, D6 -> {
        return evaluateCondition(optionParams, new BigDecimal(item.getTickness()));
      }
      case D41 -> {
        return holeDiff(item.getItemSpec()) == 0;
      }
      case D42 -> {
        return holeDiff(item.getItemSpec()) != 0;
      }
      case D5 -> {
        return routes.stream()
            .noneMatch(i -> i.getProcessNo().equals("RY") || i.getProcessNo().equals("YS"));
      }
      case Z21 -> {
        return item.getProcessRemark().contains("单刀");
      }
      case Z22 -> {
        return !item.getProcessRemark().contains("单刀");
      }
      case T1 -> {
        return BaseUtils.objtoint(item.getChannelsCount()) == 1;
      }
      case T2 -> {
        return BaseUtils.objtoint(item.getChannelsCount()) == 2;
      }
      case T3 -> {
        return BaseUtils.objtoint(item.getChannelsCount()) == 3;
      }
    }
    return true;
  }

  private static boolean evaluateCondition(String content, BigDecimal value) {
    // 使用正则表达式匹配不同的条件格式
    String singleOpRegex = "^([<>=]=?)\\s*(\\d+(\\.\\d+)?)$";
    String rangeRegex = "^(\\d+(\\.\\d+)?)\\s*-\\s*(\\d+(\\.\\d+)?)$";
    Pattern pattern1 = Pattern.compile(singleOpRegex);
    Matcher matcher1 = pattern1.matcher(content);
    if (matcher1.find()) {
      String op = BaseUtils.toString(matcher1.group(1));
      Integer v1 = BaseUtils.objtoint(matcher1.group(2));
      switch (op) {
        case ">=" -> {
          return value.intValue() >= v1;
        }
        case "<=" -> {
          return value.intValue() <= v1;
        }
        case "<" -> {
          return value.intValue() < v1;
        }
        case ">" -> {
          return value.intValue() > v1;
        }
      }
    }
    Pattern pattern2 = Pattern.compile(rangeRegex);
    Matcher matcher2 = pattern2.matcher(content);
    if (matcher2.find()) {
      Integer v1 = BaseUtils.objtoint(matcher1.group(1));
      Integer v2 = BaseUtils.objtoint(matcher1.group(3));
      return value.intValue() <= v2 && value.intValue() >= v1;
    }
    return false;
  }

  // 打孔
  private static String daKongReg = "/打.*孔/";

  // 挂孔大小区分
  private static Integer holeDiff(String spec) {
    String regex = "[（(](\\d+(\\.\\d+)?)[＋+](\\d+(\\.\\d+)?)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(spec);
    matcher.find();
    Integer i1 = BaseUtils.objtoint(matcher.group(1));
    Integer i2 = BaseUtils.objtoint(matcher.group(3));
    return Math.abs(i2 - i1);
  }

  public static void main(String[] args) {
    BigDecimal i = new BigDecimal(13).divide(new BigDecimal(2), RoundingMode.CEILING);
    System.out.println(i.intValue());
  }
}
