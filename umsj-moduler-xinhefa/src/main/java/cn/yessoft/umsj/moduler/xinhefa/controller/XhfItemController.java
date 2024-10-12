package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * 产品类型表 前端控制器
 *
 * @author ethan
 * @since 2024-09-19
 */
@RestController
@RequestMapping("/api/xhf/iteminfo")
public class XhfItemController {

  @Resource private IXhfItemService xhfItemService;

  @PostMapping("/paged-query")
  public ApiResult<PageResult<ItemVO>> query(@Valid @RequestBody ItemQueryVO reqVO) {
    PageResult<XhfItemDO> pageResult = xhfItemService.pagedQuery(reqVO);
    return success(
        new PageResult<>(
            BeanUtils.toBean(pageResult.getList(), ItemVO.class), pageResult.getTotal()));
  }

  @PostMapping("/list-query")
  public ApiResult<List<ItemVO>> queryList(@Valid @RequestBody ItemQueryVO reqVO) {
    List<XhfItemDO> r = xhfItemService.listQuery(reqVO);
    return success(BeanUtils.toBean(r, ItemVO.class));
  }

  @PostMapping("/update")
  public ApiResult<String> update(@Valid @RequestBody ItemVO reqVO) {
    xhfItemService.update(reqVO);
    return success();
  }

  @GetMapping()
  public ApiResult<ItemVO> get(@RequestParam("id") Long id) {
    XhfItemDO r = xhfItemService.validateExist(id);
    return success(BeanUtils.toBean(r, ItemVO.class));
  }
}
