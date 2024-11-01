package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import cn.yessoft.umsj.moduler.xinhefa.utils.BigDecimalSerialize;
import cn.yessoft.umsj.moduler.xinhefa.utils.XHFUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** 生产工单页面 */
@Getter
@Setter
public class MoHeaderDTO {

  private Long id;

  @JsonFormat(pattern = "MM-dd")
  private LocalDateTime moDate;

  private String cusNameShort;

  private String itemName;

  private String itemNo;

  private String itemType;

  private String itemType2;

  private BigDecimal itemGWeight;

  private BigDecimal itemLength;

  private String orderNo;

  private Integer batchNo;

  /** 入库需求数量 */
  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal qty;

  /** 投入米数 */
  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal printInputQty;

  private String apsStatus;

  private String apsStatusStr;

  /** 备注 */
  private String moRemark;

  private String soRemark;

  /** 销售单位 */
  private String purchasingNuit;

  /** 单号项次 */
  private String soDetailNumber;

  private List<MoHeaderDTO> children;

  private BigDecimal addtionRate;

  private String addtionRateStr;

  @JsonFormat(pattern = "MM-dd")
  private LocalDateTime requiedFinishDate;

  @JsonFormat(pattern = "MM-dd")
  private LocalDateTime requiedStartDate;

  private Integer week;

  private String ysMaterialConfirmDateStr;

  private String fhMaterialConfirmDateStr;

  private String ysStartDateStr;
  private String pmStartDateStr;
  private String fhStartDateStr;
  private String jmStartDateStr;
  private String tjStartDateStr;
  private String kmStartDateStr;
  private String zxStartDateStr;
  private String zjStartDateStr;
  private String finishDateStr;

  private XHFUtils.ProductUnitConvert qtyConvert;
}
