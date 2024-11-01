package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 机台产能参数
 *
 * @author ethan
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("xhf_machine_property")
public class XhfMachinePropertyDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 机台ID */
  private Long machineId;

  /** 区分项目1 */
  private String ruleOption1;

  /** 区分内容1 */
  private String ruleOptionContent1;

  /** 区分项目2 */
  private String ruleOption2;

  /** 区分内容2 */
  private String ruleOptionContent2;

  /** 卷长 */
  private BigDecimal rollLength;

  /** 机速 */
  private BigDecimal speed;

  /** 机速单位 */
  private String speedUnit;

  /** 换卷时间 */
  private BigDecimal changeRollTime;

  /** 换型时间 */
  private Integer changeTypeTime;

  /** 换色时间 */
  private Integer changeColorTime;

  /** 换墨时间 */
  private Integer changeOilTime;

  /** 换胶时间 */
  private Integer changeGluTime;

  /** 效率 */
  private BigDecimal efficiency;

  private Integer seq;
}
