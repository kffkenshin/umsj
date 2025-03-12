package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.MachineDisablePlanCreateVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MachineDisableInfoDTO;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachineDisablePlanService;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * 停机计划 前端控制器
 *
 * @author ethan
 * @since 2024-11-07
 */
@RestController
@RequestMapping("/api/xhf/machine-disable-plan")
public class XhfMachineDisablePlanController {
  @Resource IXhfMachineDisablePlanService xhfMachineDisablePlanService;

  @GetMapping("/info")
  public ApiResult<List<MachineDisableInfoDTO>> getMachineDisablePlanInfo(
      @RequestParam("machineNo") String machineNo, @RequestParam("day") String day) {
    return success(xhfMachineDisablePlanService.getMachineDisablePlan(machineNo, day));
  }

  @PostMapping("/create")
  public ApiResult<String> createMachine(@RequestBody MachineDisablePlanCreateVO reqVO) {
    xhfMachineDisablePlanService.createPlans(reqVO);
    return success();
  }
}
