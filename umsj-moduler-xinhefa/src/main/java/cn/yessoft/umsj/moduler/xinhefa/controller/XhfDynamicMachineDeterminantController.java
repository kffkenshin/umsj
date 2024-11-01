package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.DynamicMachineDeterminantVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.DynamicMachineQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfDynamicMachineDeterminantDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFWorkStationEnum;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfDynamicMachineDeterminantService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 规则选机 前端控制器
 *
 * @author ethan
 * @since 2024-10-19
 */
@RestController
@RequestMapping("/api/xhf/dynamic-determing")
public class XhfDynamicMachineDeterminantController {
  @Resource private IXhfDynamicMachineDeterminantService xhfDynamicMachineDeterminantService;

  @PostMapping("/paged-query")
  public ApiResult<PageResult<DynamicMachineDeterminantVO>> query(
      @RequestBody DynamicMachineQueryReqVO reqVO) {
    PageResult<XhfDynamicMachineDeterminantDO> pageResult =
        xhfDynamicMachineDeterminantService.pagedQuery(reqVO);
    return success(
        new PageResult<>(
            BeanUtils.toBean(
                pageResult.getList(),
                DynamicMachineDeterminantVO.class,
                entity -> {
                  if (entity.getWorkStation() != null) {
                    entity.setWorkStationStr(
                        XHFWorkStationEnum.valueOf(entity.getWorkStation()).getName());
                  }
                }),
            pageResult.getTotal()));
  }
}
