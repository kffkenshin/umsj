package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.MachineInfoVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.MachinePropertyVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachinePropertyDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFWorkStationEnum;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachinePropertyService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachineService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * 机台表 前端控制器
 *
 * @author ethan
 * @since 2024-10-17
 */
@RestController
@RequestMapping("/api/xhf/machine-info")
public class XhfMachineController {
  @Resource private IXhfMachineService xhfMachineService;
  @Resource private IXhfMachinePropertyService xhfMachinePropertyService;

  @PostMapping("/paged-query")
  public ApiResult<PageResult<MachineInfoVO>> machinInfoQuery(
      @Valid @RequestBody InfoPageQueryReqVO reqVO) {
    PageResult<XhfMachineDO> pageResult = xhfMachineService.pagedQuery(reqVO);
    return success(
        new PageResult<>(
            BeanUtils.toBean(
                pageResult.getList(),
                MachineInfoVO.class,
                xhfMachineVO -> {
                  if (xhfMachineVO.getWorkStation() != null) {
                    xhfMachineVO.setWorkStationStr(
                        XHFWorkStationEnum.valueOf(xhfMachineVO.getWorkStation()).getName());
                  }
                }),
            pageResult.getTotal()));
  }

  @GetMapping("/property/list")
  public ApiResult<List<MachinePropertyVO>> getMachinePropertyList(@RequestParam("id") Long id) {
    List<XhfMachinePropertyDO> result = xhfMachinePropertyService.getListByMachineId(id);
    return success(BeanUtils.toBean(result, MachinePropertyVO.class));
  }

  @PostMapping("/delete")
  public ApiResult<String> deleteMachine(@RequestBody List<Long> ids) {
    xhfMachineService.delete(ids);
    return success();
  }

  @PostMapping("/property/delete")
  public ApiResult<String> deleteMachineProperty(@RequestBody List<Long> ids) {
    xhfMachinePropertyService.delete(ids);
    return success();
  }
}
