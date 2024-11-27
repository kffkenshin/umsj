package cn.yessoft.umsj.moduler.xinhefa.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDisablePlanDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachinePropertyDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderDetailDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFMachineSpeedUnitEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFProductUnitEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFWorkStationEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.Data;

public class XHFUtils {
  /** 1000 1千 */
  public static final BigDecimal K = new BigDecimal(1000);

  /** 10000 1万 */
  public static final BigDecimal W = new BigDecimal(10000);

  /** 100 1百 */
  public static final BigDecimal B = new BigDecimal(100);

  /** 24 */
  public static final BigDecimal DAYHOUR = new BigDecimal(24);

  public static ProductUnitConvert getProductUnitConvert(XhfItemDO item, BigDecimal qty) {
    BigDecimal length = new BigDecimal(item.getLength() == null ? 1 : item.getLength().intValue());
    BigDecimal gw = item.getGWeight() == null ? new BigDecimal(1) : item.getGWeight();
    return getProductUnitConvert(length, gw, item.getSaleUnit(), qty);
  }

  public static ProductUnitConvert getProductUnitConvert(
      BigDecimal length, BigDecimal gw, String saleUnit, BigDecimal qty) {
    ProductUnitConvert result = new ProductUnitConvert();
    switch (XHFProductUnitEnum.valueof(saleUnit)) {
      case PCS -> {
        result.setPcs(qty);
      }
      case KG -> {
        result.setPcs(qty.multiply(K).divide(gw, 3, BigDecimal.ROUND_HALF_UP));
      }
      case KM -> {
        result.setPcs(qty.multiply(K).multiply(K).divide(length, 3, BigDecimal.ROUND_HALF_UP));
      }
      case M -> {
        result.setPcs(qty.multiply(K).divide(length, 3, BigDecimal.ROUND_HALF_UP));
      }
      case KPCS -> {
        result.setPcs(qty.multiply(K));
      }
      case WPCS -> {
        result.setPcs(qty.multiply(W));
      }
      default -> {}
    }
    result.setWpcs(result.getPcs().divide(W, 3, BigDecimal.ROUND_HALF_UP));
    result.setKpcs(result.getPcs().divide(K, 3, BigDecimal.ROUND_HALF_UP));
    result.setKg(result.getPcs().multiply(gw).divide(K, 3, BigDecimal.ROUND_HALF_UP));
    result.setM(result.getPcs().multiply(length));
    result.setKm(result.getM().divide(K, 3, BigDecimal.ROUND_HALF_UP));
    switch (XHFProductUnitEnum.valueof(saleUnit)) {
      case PCS -> {
        result.setPcs(qty);
      }
      case KG -> {
        result.setKg(qty);
      }
      case KM -> {
        result.setKm(qty);
      }
      case M -> {
        result.setM(qty);
      }
      case KPCS -> {
        result.setKpcs(qty);
      }
      case WPCS -> {
        result.setWpcs(qty);
      }
      default -> {}
    }
    return result;
  }

  // 计算切换时间 (分钟) todo 换油墨  换版辊
  public static BigDecimal caculateChangeTime(
      XhfItemDO item,
      XhfItemDO nextItem,
      XhfMachinePropertyDO machineProperty,
      Integer workStation,
      BigDecimal qty) {
    BigDecimal huanxing = BigDecimal.ZERO;
    BigDecimal change = BigDecimal.ZERO;
    if (!item.getId().equals(nextItem.getId())) {
      huanxing = new BigDecimal(machineProperty.getChangeTypeTime());
    }
    switch (XHFWorkStationEnum.valueOf(workStation)) {
      case YS -> { // 切换时间=[换型切换时间+（换色切换时间+换油墨体系时间）*版辊差异数]/60
        BigDecimal bangunchayi = BigDecimal.ZERO;
        BigDecimal huansehuanyoumo =
            new BigDecimal(
                machineProperty.getChangeColorTime() + machineProperty.getChangeOilTime());
        change = huanxing.add(huansehuanyoumo.multiply(bangunchayi));
      }
      case PM, JM, KM, ZDJ -> { // 切换时间=（换型切换时间+换卷切换时间X投入卷数）/60
        change = huanxing.add(qty.multiply(machineProperty.getChangeRollTime()));
      }
      case FH -> { // 切换时间=（换型切换时间+换复合胶水时间+换卷切换时间X投入卷数）/60
        change = huanxing.add(qty.multiply(machineProperty.getChangeRollTime()));
        change = change.add(new BigDecimal(machineProperty.getChangeGluTime()));
      }
      case TJ -> { // 切换时间=[（换型切换时间+换卷切换时间X投入卷数）*向上取整(通道数/2)]/60
        change =
            huanxing.add(
                qty.multiply(machineProperty.getChangeRollTime())
                    .divide(new BigDecimal(item.getChannelsCount() / 2)));
      }
      case ZDX -> { // 切换时间=（换型切换时间+换卷切换时间X投入卷数*通道数）/60
        change =
            huanxing.add(
                qty.multiply(machineProperty.getChangeRollTime())
                    .divide(new BigDecimal(item.getChannelsCount())));
      }
      default -> {}
    }
    return change;
  }

  // 计算停机时间(分钟)
  public static BigDecimal caculateStopTime(
      XhfManufactureOrderDetailDO moDetail, List<XhfMachineDisablePlanDO> machineDisablePlans) {
    LocalDateTime stoped =
        moDetail
            .getStartTime()
            .plusMinutes(moDetail.getChangeTime().longValue())
            .plusMinutes(moDetail.getProductionTime().longValue());
    BigDecimal result = BigDecimal.ZERO;
    for (XhfMachineDisablePlanDO plan : machineDisablePlans) {
      if (plan.getStartTime().isAfter(moDetail.getStartTime())
          && plan.getStartTime().isBefore(stoped)) {
        BigDecimal stoptime =
            new BigDecimal(
                LocalDateTimeUtil.between(plan.getStartTime(), plan.getEndTime()).getSeconds()
                    / 60);
        result = result.add(stoptime);
        stoped = stoped.plusMinutes(stoptime.longValue());
      }
    }
    return result;
  }

  @Data
  public static class ProductUnitConvert {
    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal kg;

    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal km;

    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal m;

    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal pcs;

    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal kpcs;

    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal wpcs;
  }

  // 计算生产时间 (分钟)
  public static BigDecimal caculateProductTime(
      BigDecimal printQty,
      BigDecimal speed,
      String speedUnit,
      BigDecimal speedEffective,
      XhfItemDO item) {
    switch (XHFMachineSpeedUnitEnum.valueOf(speedUnit.toLowerCase())) {
      case PCS -> { // 投入米数 *1000 / 袋长 /机速 *效率/60
        return printQty
            .multiply(K)
            .divide(new BigDecimal(item.getLength()))
            .divide(speed)
            .multiply(speedEffective);
      }
      case LENGTH -> { // 投入米数 / 机速 * 效率 / 60
        return printQty.divide(speed).multiply(speedEffective);
      }
      case KG -> { // 投入米数 *1000 / 袋长 * 克重/1000 /机速 *效率/60
        return printQty
            .divide(new BigDecimal(item.getLength()))
            .multiply(item.getGWeight())
            .divide(speed)
            .multiply(speedEffective);
      }
      default -> {
        return BigDecimal.ZERO;
      }
    }
  }

  public static LocalDateTime getWeekBegin() {
    return LocalDateTimeUtil.of(DateUtil.beginOfWeek(new Date()));
  }

  public static void main(String[] args) {
    System.out.println("2024-11-07".substring(0, 4));
    System.out.println("2024-11-07".substring(5, 7));
    System.out.println("2024-11-07".substring(8, 10));
  }
}
