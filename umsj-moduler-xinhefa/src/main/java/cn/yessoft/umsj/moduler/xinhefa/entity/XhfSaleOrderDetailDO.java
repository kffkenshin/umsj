package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 销售订单明细表
 *
 * @author ethan
 * @since 2024-10-03
 */
@Getter
@Setter
@TableName("xhf_sale_order_detail")
public class XhfSaleOrderDetailDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 订单ID */
  private Long soId;

  /** 料号ID */
  private Long itemId;

  /** 客户料号ID */
  private Long cusItemId;

  /** 订单数量 */
  private BigDecimal qty;

  /** 已发货数 */
  private BigDecimal deliveredQuantity;

  /** T100 状态 */
  private String t100Status;

  /** APS 状态 */
  private String apsStatus;

  /** 备注 */
  private String remark;

  /** 预计发货日 */
  private LocalDateTime preDeliveryDate;

  /** 销售单位 */
  private String purchasingNuit;

  /** aprm */
  private Boolean amrpFlag;

  /** 净库存 */
  private BigDecimal netInventory;

  /** 受理时间 */
  private LocalDateTime approveTime;

  /** 初始净库存 */
  private BigDecimal dfNetInventory;

  /** 初始净库存备注 */
  private String dfNetInventoryDt;

  /** 受理者 */
  private String approveUser;

  /** 项次号 */
  private Integer detailSeq;

  /** 单号项次 */
  private String soDetailNumber;

  /** 印刷米数 */
  private BigDecimal printQty;

  /** 订单万只 * */
  private BigDecimal wpcs;
}
