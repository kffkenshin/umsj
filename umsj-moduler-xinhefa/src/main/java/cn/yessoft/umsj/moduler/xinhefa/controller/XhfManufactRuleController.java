package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFLeadTimeEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFManufactureRuleEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFNextStationWaitTimeEnum;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xhf/morule")
public class XhfManufactRuleController {

  @GetMapping("/list")
  public ApiResult<XHFManufactureRuleEnum[]> getManufactureRule() {
    return success(XHFManufactureRuleEnum.values());
  }

  @GetMapping("/lead-time/list")
  public ApiResult<XHFLeadTimeEnum[]> getLeadTime() {
    return success(XHFLeadTimeEnum.values());
  }

  @GetMapping("/next-station-wait-time/list")
  public ApiResult<XHFNextStationWaitTimeEnum[]> getNextStationWaitTime() {
    return success(XHFNextStationWaitTimeEnum.values());
  }
}
