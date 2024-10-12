package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import cn.yessoft.umsj.moduler.xinhefa.utils.BigDecimalSerialize;
import cn.yessoft.umsj.moduler.xinhefa.utils.BigDecimalToIntSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import lombok.Data;

/** 试算结果 */
@Data
public class SimulateDetailDTO {
  private String workStationStr;
  private Integer workStation;
  private Integer seq;
  private String processName;
  private String processNo;

  @JsonSerialize(using = BigDecimalToIntSerialize.class)
  private BigDecimal prepareQty1;

  @JsonSerialize(using = BigDecimalToIntSerialize.class)
  private BigDecimal prepareQty2;

  private BigDecimal processRejectRate;

  private BigDecimal abnormalRejectRate;

  private BigDecimal actAbnormalRejectRate;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal inputQty1;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal inputQty2;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal outputQty;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal processRejectQty;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal abnormalRejectQty;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal actAbnormalRejectQty;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal totalRejectQty;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal actTotalRejectQty;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal totalRejectRate;
}
