package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineInfoVO {

  private Long id;

  /** 所属工作站 */
  private Integer workStation;

  private String workStationStr;

  /** 序号 */
  private Integer seq;

  /** 编号 */
  private String machineNo;

  /** 名称 */
  private String machineName;
}
