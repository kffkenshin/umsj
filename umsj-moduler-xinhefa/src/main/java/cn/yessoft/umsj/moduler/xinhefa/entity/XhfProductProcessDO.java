package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 生产工艺表
 *
 * @author ethan
 * @since 2024-09-20
 */
@Getter
@Setter
@TableName("xhf_product_process")
public class XhfProductProcessDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 工艺编号 */
  private String processNo;

  /** 名称 */
  private String processName;

  /** 所属工段 */
  private Integer workStation;

  private BigDecimal prepareRejectRate;

  private BigDecimal processRejectRate;

  private BigDecimal abnormalRejectRate;
}
