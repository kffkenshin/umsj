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
 * 生产订单
 *
 * @author ethan
 * @since 2024-10-23
 */
@Getter
@Setter
@TableName("xhf_manufacture_order_header")
public class XhfManufactureOrderHeaderDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 生产产品 */
  private Long itemId;

  /** 生产数量 */
  private BigDecimal qty;

  /** 工单号码 */
  private String orderNo;

  /** 放量 */
  private BigDecimal addtionRate;

  /** 备注 */
  private String remark;

  /** 客户ID */
  private Long customerId;

  /** 状态 */
  private String status;

  /** 工单类型 */
  private String type;

  private LocalDateTime moDate;

  private String soDetailNumber;
}
