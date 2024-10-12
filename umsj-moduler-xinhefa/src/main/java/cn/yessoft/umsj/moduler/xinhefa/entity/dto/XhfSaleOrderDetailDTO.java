package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import cn.yessoft.umsj.moduler.xinhefa.utils.BigDecimalSerialize;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/** 销售订单明细表 */
@Getter
@Setter
public class XhfSaleOrderDetailDTO {

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @JsonFormat(pattern = "MM-dd")
  private LocalDateTime orderDate;

  private String cusNameShort;

  private String itemName;

  private String itemNo;

  private String itemType;

  private String itemType2;

  /** 订单数量 */
  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal qty;

  private String apsStatus;

  private String apsStatusStr;

  private String cusItemNo;

  /** 备注 */
  private String remark;

  /** 预计发货日 */
  @JsonFormat(pattern = "MM-dd")
  private LocalDateTime preDeliveryDate;

  /** 销售单位 */
  private String purchasingNuit;

  /** aprm */
  private Boolean amrpFlag;

  /** 净库存 */
  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal netInventory;

  /** 初始净库存 */
  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal dfNetInventory;

  /** 初始净库存备注 */
  private String dfNetInventoryDt;

  /** 项次号 */
  private Integer detailSeq;

  /** 单号项次 */
  private String soDetailNumber;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal printQty;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal requireQty;

  private String customerOrderNo;

  @JsonSerialize(using = BigDecimalSerialize.class)
  private BigDecimal wpcs;
}
