package cn.yessoft.umsj.moduler.xinhefa.utils;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFMachineSpeedUnitEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFProductUnitEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import lombok.Data;

public class XHFUtils {
  /** 1000 1千 */
  public static final BigDecimal K = new BigDecimal(1000);

  /** 10000 1万 */
  public static final BigDecimal W = new BigDecimal(10000);

  /** 100 1百 */
  public static final BigDecimal B = new BigDecimal(100);

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
            .multiply(speedEffective)
            .divide(new BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP);
      }
      case LENGTH -> { // 投入米数 / 机速 * 效率 / 60
        return printQty
            .divide(speed)
            .multiply(speedEffective)
            .divide(new BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP);
      }
      case KG -> { // 投入米数 *1000 / 袋长 * 克重/1000 /机速 *效率/60
        return printQty
            .divide(new BigDecimal(item.getLength()))
            .multiply(item.getGWeight())
            .divide(speed)
            .multiply(speedEffective)
            .divide(new BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP);
      }
      default -> {
        return BigDecimal.ZERO;
      }
    }
  }
}
