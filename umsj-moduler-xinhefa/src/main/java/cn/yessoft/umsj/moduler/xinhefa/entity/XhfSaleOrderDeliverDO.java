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
 * 订单批次
 *
 * @author ethan
 * @since 2024-10-15
 */
@Getter
@Setter
@TableName("xhf_sale_order_deliver")
public class XhfSaleOrderDeliverDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 单头ID */
  private Long headerId;

  /** 单身ID */
  private Long detailId;

  /** 预计交期 */
  private LocalDateTime deliverDate;

  /** 交货数量 */
  private BigDecimal qty;

  /** 状态 */
  private String status;

  /** 批序号 */
  private Integer batchNo;

  /** 净库存 */
  private BigDecimal netInventoryQty;

  /** 物流天数 */
  private Integer logisticsDays;

  /** 入库提前 */
  private Integer warehousingLeadDays;
}
