package cn.yessoft.umsj.moduler.xinhefa.utils;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFProductUnitEnum;
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
    ProductUnitConvert result = new ProductUnitConvert();
    BigDecimal length = new BigDecimal(item.getLength() == null ? 1 : item.getLength().intValue());
    BigDecimal gw = item.getGWeight() == null ? new BigDecimal(1) : item.getGWeight();
    switch (XHFProductUnitEnum.valueof(item.getSaleUnit())) {
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
    switch (XHFProductUnitEnum.valueof(item.getSaleUnit())) {
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
    private BigDecimal kg;
    private BigDecimal km;
    private BigDecimal m;
    private BigDecimal pcs;
    private BigDecimal kpcs;
    private BigDecimal wpcs;
  }
}
