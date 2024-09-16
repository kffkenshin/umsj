package cn.yessoft.umsj.moduler.base.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.menu.MenuQueryReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.menu.MenuRespVO;
import cn.yessoft.umsj.moduler.base.controller.vo.menu.MenuSwitchReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseMenuDO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseMenuDTO;
import cn.yessoft.umsj.moduler.base.service.IBaseMenuService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

/**
 * <p>
 * 菜单及功能表 前端控制器
 * </p>
 *
 * @author ethan
 * @since 2024-08-31
 */
@RequestMapping("/api/base/menu")
@RestController
public class BaseMenuController {
    @Resource
    private IBaseMenuService baseMenuService;

    @PostMapping("/paged-query")
    public ApiResult<PageResult<MenuRespVO>> query(@Valid @RequestBody MenuQueryReqVO reqVO) {
        PageResult<BaseMenuDTO> pageResult = baseMenuService.pagedQuery(reqVO);
        return success(new PageResult<>(BeanUtils.toBean(pageResult.getList(), MenuRespVO.class),
                pageResult.getTotal()));
    }

    @GetMapping("/get")
    public ApiResult<MenuRespVO> getMenu(@RequestParam("id") Long id) {
        BaseMenuDO menu = baseMenuService.getMenu(id);
        if (menu == null) {
            return success(null);
        }
        return success(BeanUtils.toBean(menu, MenuRespVO.class));
    }

    @PostMapping("/switch")
    public ApiResult<String> query(@Valid @RequestBody MenuSwitchReqVO reqVO) {
        baseMenuService.switchEnable(reqVO.getId());
        return success("ok");
    }
}
