package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.EnumSelector;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.mo.MoHeaderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MoHeaderDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFMOHeaderStatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfManufactureOrderHeaderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * 生产订单 前端控制器
 *
 * @author ethan
 * @since 2024-10-23
 */
@RestController
@RequestMapping("/api/xhf/mo")
public class XhfManufactureOrderController {
  @Resource private IXhfManufactureOrderHeaderService xhfMoHeaderService;

  @PostMapping("/create-from-so")
  public ApiResult<String> createMoFromSo(@RequestBody List<Long> ids) {
    xhfMoHeaderService.createFromSoDeliver(ids);
    return success();
  }

  @GetMapping("/header/status")
  public ApiResult<List<EnumSelector>> getMoHaederStatusForSelector() {
    return success(XHFMOHeaderStatusEnum.getSelector());
  }

  @PostMapping("/header/paged-query")
  public ApiResult<PageResult<MoHeaderDTO>> moHeaderQuery(
      @Valid @RequestBody MoHeaderQueryReqVO reqVO) {
    PageResult<MoHeaderDTO> pageResult = xhfMoHeaderService.pagedQuery(reqVO);
    return success(pageResult);
  }
}
