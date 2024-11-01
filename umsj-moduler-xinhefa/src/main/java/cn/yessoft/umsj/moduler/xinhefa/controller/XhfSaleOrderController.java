package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.EnumSelector;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderDeliverCreateVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.XhfSoDeliverViewVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSoDeliverViewDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfSaleOrderDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSODeliverStatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFSODetailAPSStatusEnum;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSaleOrderDeliverService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSaleOrderDetailService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSoDeliverViewService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * 销售订单表 前端控制器
 *
 * @author ethan
 * @since 2024-10-03
 */
@RestController
@RequestMapping("/api/xhf/so")
public class XhfSaleOrderController {

  @Resource private IXhfSaleOrderDetailService xhfSaleOrderDetailService;
  @Resource private IXhfSaleOrderDeliverService xhfSaleOrderDeliverService;
  @Resource private IXhfSoDeliverViewService xhfSoDeliverViewService;

  @GetMapping("/detail/apsstatus")
  public ApiResult<List<EnumSelector>> getDetailApsStatusForSelector() {
    return success(XHFSODetailAPSStatusEnum.getSelector());
  }

  @GetMapping("/deliver/status")
  public ApiResult<List<EnumSelector>> getDeliverStatusForSelector() {
    return success(XHFSODeliverStatusEnum.getSelector());
  }

  @PostMapping("/detail/paged-query")
  public ApiResult<PageResult<XhfSaleOrderDetailDTO>> soDetailQuery(
      @Valid @RequestBody SaleOrderQueryReqVO reqVO) {
    PageResult<XhfSaleOrderDetailDTO> pageResult = xhfSaleOrderDetailService.pagedQuery(reqVO);
    pageResult
        .getList()
        .forEach(
            i -> {
              i.setApsStatusStr(XHFSODetailAPSStatusEnum.valueOfNo(i.getApsStatus()).getName());
              i.setRequireQty(i.getQty().subtract(i.getNetInventory()));
              i.setPreDeliveryDate2(i.getPreDeliveryDate());
            });
    return success(pageResult);
  }

  @PostMapping("/deliver/paged-query")
  public ApiResult<PageResult<XhfSoDeliverViewVO>> soDeliverQuery(
      @Valid @RequestBody SaleOrderQueryReqVO reqVO) {
    PageResult<XhfSoDeliverViewDO> pageResult = xhfSoDeliverViewService.pagedQuery(reqVO);
    return success(
        new PageResult<>(
            BeanUtils.toBean(pageResult.getList(), XhfSoDeliverViewVO.class),
            pageResult.getTotal()));
  }

  @PostMapping("/deliver/batch-create")
  public ApiResult<String> batchCreateDeliver(
      @Valid @RequestBody List<SaleOrderDeliverCreateVO> reqVO) {
    xhfSaleOrderDeliverService.batchCreate(reqVO);
    return success();
  }
}
