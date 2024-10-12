package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ProductProcessVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.RejectRateSimulateVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.RejectRateSimulateDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFWorkStationEnum;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductProcessService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产工艺表 前端控制器
 *
 * @author ethan
 * @since 2024-09-20
 */
@RestController
@RequestMapping("/api/xhf/product-process")
public class XhfProductProcessController {

  @Resource private IXhfProductProcessService xhfProductProcessService;

  @PostMapping("/paged-query")
  public ApiResult<PageResult<ProductProcessVO>> query(
      @Valid @RequestBody InfoPageQueryReqVO reqVO) {
    PageResult<XhfProductProcessDO> pageResult = xhfProductProcessService.pagedQuery(reqVO);
    return success(
        new PageResult<>(
            BeanUtils.toBean(
                pageResult.getList(),
                ProductProcessVO.class,
                v -> {
                  if (BaseUtils.isNotEmpty(v.getWorkStation())) {
                    v.setWorkStationStr(
                        XHFWorkStationEnum.valueOf(v.getWorkStation()) == null
                            ? ""
                            : XHFWorkStationEnum.valueOf(v.getWorkStation()).getName());
                  } else {
                    v.setWorkStationStr("");
                  }
                }),
            pageResult.getTotal()));
  }

  @PostMapping("/update-reject")
  public ApiResult<String> updateRates(@Valid @RequestBody ProductProcessVO reqVO) {
    xhfProductProcessService.updateRates(reqVO);
    return success();
  }

  @PostMapping("/simulate")
  public ApiResult<RejectRateSimulateDTO> simulate(@Valid @RequestBody RejectRateSimulateVO reqVO) {
    return success(xhfProductProcessService.simulate(reqVO));
  }
}
