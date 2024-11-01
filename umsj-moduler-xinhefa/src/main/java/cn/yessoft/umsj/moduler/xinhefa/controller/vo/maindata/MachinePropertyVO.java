package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

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
public class MachinePropertyVO {

  private Long id;

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
