package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemTypeQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemTypeRespVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemTypeDO;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemTypeService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * 产品类型表 前端控制器
 *
 * @author ethan
 * @since 2024-09-16
 */
@RestController
@RequestMapping("/api/xhf/itemtype")
public class XhfItemTypeController {
  @Resource private IXhfItemTypeService xhfItemTypeService;

  @PostMapping("/paged-query")
  public ApiResult<PageResult<ItemTypeRespVO>> query(@Valid @RequestBody ItemTypeQueryReqVO reqVO) {
    PageResult<XhfItemTypeDO> pageResult = xhfItemTypeService.pagedQuery(reqVO);
    return success(
        new PageResult<>(
            BeanUtils.toBean(pageResult.getList(), ItemTypeRespVO.class), pageResult.getTotal()));
  }

  @PostMapping("/list-query")
  public ApiResult<List<ItemTypeRespVO>> queryList(@Valid @RequestBody ItemTypeQueryReqVO reqVO) {
    List<XhfItemTypeDO> r = xhfItemTypeService.listQuery(reqVO);
    return success(BeanUtils.toBean(r, ItemTypeRespVO.class));
  }

  @PostMapping("/create")
  public ApiResult<String> create(@Valid @RequestBody ItemTypeRespVO reqVO) {
    xhfItemTypeService.create(reqVO);
    return success();
  }

  @PostMapping("/update")
  public ApiResult<String> update(@Valid @RequestBody ItemTypeRespVO reqVO) {
    xhfItemTypeService.update(reqVO);
    return success();
  }

  @GetMapping()
  public ApiResult<ItemTypeRespVO> get(@RequestParam("id") Long id) {
    XhfItemTypeDO r = xhfItemTypeService.validateExist(id);
    return success(BeanUtils.toBean(r, ItemTypeRespVO.class));
  }

  @PostMapping("/delete")
  public ApiResult<String> delete(@RequestBody List<Long> ids) {
    xhfItemTypeService.delete(ids);
    return success();
  }
}
