package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineDisablePlanCreateVO {

  private List<Long> machines;

  private String startTime;

  private String endTime;

  private List<String> dateRange;

  private List<Integer> week;

  private Boolean nextDay;
}
