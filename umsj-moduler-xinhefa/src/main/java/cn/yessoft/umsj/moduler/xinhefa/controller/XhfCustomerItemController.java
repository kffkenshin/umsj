package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfCustomerItemDTO;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerItemService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户物料表 前端控制器
 *
 * @author ethan
 * @since 2024-09-24
 */
@RestController
@RequestMapping("/api/xhf/customer-item")
public class XhfCustomerItemController {

  @Resource private IXhfCustomerItemService xhfCustomerItemService;

  @PostMapping("/paged-query")
  public ApiResult<PageResult<XhfCustomerItemDTO>> query(@RequestBody InfoPageQueryReqVO reqVO) {
    PageResult<XhfCustomerItemDTO> pageResult = xhfCustomerItemService.pagedQuery(reqVO);
    return success(pageResult);
  }
}
