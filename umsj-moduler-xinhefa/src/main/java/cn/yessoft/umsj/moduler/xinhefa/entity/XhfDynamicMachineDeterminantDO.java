package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 规则选机
 *
 * @author ethan
 * @since 2024-10-19
 */
@Getter
@Setter
@TableName("xhf_dynamic_machine_determinant")
public class XhfDynamicMachineDeterminantDO extends BaseDO {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 工作站 */
  private Integer workStation;

  /** 产品类型 */
  private String itemType;

  /** 父产线ID */
  private String machineNo;

  /** 客户名称 */
  private String customerName;

  /** 区分代码1 */
  private String optionCode1;

  /** 区分内容1 */
  private String optionParams1;

  /** 区分代码2 */
  private String optionCode2;

  /** 区分内容2 */
  private String optionParams2;

  /** 区分代码3 */
  private String optionCode3;

  /** 区分内容3 */
  private String optionParams3;

  /** 默认台数 */
  private Byte defaultMachineCount;

  /** 首选机台号 */
  private String firstMachineNo;

  /** 可选机台号1 */
  private String sencondMachineNo1;

  /** 可选机台号2 */
  private String sencondMachineNo2;

  /** 可选机台号3 */
  private String sencondMachineNo3;

  /** 可选机台号4 */
  private String sencondMachineNo4;

  /** 可选机台号5 */
  private String sencondMachineNo5;

  /** 可选机台号6 */
  private String sencondMachineNo6;

  /** 可选机台号7 */
  private String sencondMachineNo7;

  /** 可选机台号8 */
  private String sencondMachineNo8;
}
