package cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * VIEW
 *
 * @author ethan
 * @since 2024-10-16
 */
@Getter
@Setter
public class XhfSoDeliverViewVO {

  /** ID */
  private Long id;

  /** 厂内料号 */
  private String itemNo;

  /** 名称 */
  private String itemName;

  /** 长度 */
  private Integer itemLength;

  /** 克重 */
  private BigDecimal itemGweight;

  /** 销售单位 */
  private String itemSaleUnit;

  /** 订单日期 */
  @JsonFormat(pattern = "MM-dd")
  private LocalDateTime orderDate;

  /** 单号项次 */
  private String detailNumber;

  /** 批序号 */
  private Integer batchNo;

  private Long batchCount;

  /** 状态 */
  private String deliverStatus;

  /** 客户简称 */
  private String cusNameShort;

  /** 客户料号编号 */
  private String customerItemNo;

  /** 预计交期 */
  @JsonFormat(pattern = "MM-dd")
  private LocalDateTime deliverDate;

  /** 交货数量 */
  private BigDecimal qty;

  /** 订单数量 */
  private BigDecimal detailQty;

  /** 净库存 */
  private BigDecimal detailNetInventory;

  /** 净库存 */
  private BigDecimal deliverNetInventory;

  /** 备注 */
  private String remark;

  /** 产品类型2 */
  private String itemType;
}
