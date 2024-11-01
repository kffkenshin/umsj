package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import cn.yessoft.umsj.common.pojo.PageParam;
import java.util.List;
import lombok.Data;

@Data
public class DynamicMachineQueryReqVO extends PageParam {
  private List<Integer> workStation;
}
