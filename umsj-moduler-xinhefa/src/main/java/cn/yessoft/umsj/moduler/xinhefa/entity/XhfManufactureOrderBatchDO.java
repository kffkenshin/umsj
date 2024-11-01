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
 * 生产订单批次
 *
 * @author ethan
 * @since 2024-10-23
 */
@Getter
@Setter
@TableName("xhf_manufacture_order_batch")
public class XhfManufactureOrderBatchDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 生产工单ID */
  private Long headerId;

  /** 状态 */
  private Integer status;

  /** 入库需求时间 */
  private LocalDateTime requireDate;

  /** 工单批号 */
  private Integer batchNo;

  /** 来源单据ID * */
  private Long relatedDocId;

  private BigDecimal batchQty;
}
