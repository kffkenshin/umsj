package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 销售订单表
 *
 * @author ethan
 * @since 2024-10-03
 */
@Getter
@Setter
@TableName("xhf_sale_order_header")
public class XhfSaleOrderHeaderDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 客户ID */
  private Long customerId;

  /** 客户单号 */
  private String cusSaleOrderNo;

  /** 订单号 */
  private String orderNo;

  /** 状态 */
  private String status;

  /** 订单日期 */
  private LocalDateTime orderDate;
}
