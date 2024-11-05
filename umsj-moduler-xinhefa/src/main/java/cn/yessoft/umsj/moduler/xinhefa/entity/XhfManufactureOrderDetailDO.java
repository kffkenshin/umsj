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
 * 生产订单明细
 *
 * @author ethan
 * @since 2024-10-23
 */
@Getter
@Setter
@TableName("xhf_manufacture_order_detail")
public class XhfManufactureOrderDetailDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 工单ID */
  private Long headerId;

  /** 投入米数 */
  private BigDecimal inputQty;

  /** 产出米数 */
  private BigDecimal outputQty;

  /** 工作站 */
  private Integer workStation;

  /** 状态 */
  private String status;

  /** 开始时间 */
  private LocalDateTime startTime;

  /** 结束时间 */
  private LocalDateTime endTime;

  /** 生产提前期 */
  private BigDecimal leadTime;

  /** 最早开始时间 */
  private LocalDateTime earliestStartTime;

  /** 切换时间 */
  private BigDecimal changeTime;

  /** 完成数量 */
  private BigDecimal finishedQty;

  /** 机速 */
  private BigDecimal speed;

  /** 机速单位 */
  private String speedUnit;

  /** 机速效率 */
  private BigDecimal speedEfficiency;

  /** 序号 */
  private Byte seq;

  /** 机台号 */
  private String machineNumber;

  /** 准备米数 */
  private BigDecimal prepareRejectQty;

  /** 过程废品率 */
  private BigDecimal processRejectRate;

  /** 异常废品率 */
  private BigDecimal abnormalRejectRate;

  /** 批次ID */
  private Long batchId;

  private Long parentDetailId;
}
