package cn.yessoft.umsj.moduler.base.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.moduler.base.controller.vo.rolepermission.RolePemissionListReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.rolepermission.RolePemissionSwitchReqVO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseRolePermissionDTO;
import cn.yessoft.umsj.moduler.base.enums.BaseConstants;
import cn.yessoft.umsj.moduler.base.service.IBaseRolePermissionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

/**
 * <p>
 * 角色权限表 前端控制器
 * </p>
 *
 * @author ethan
 * @since 2024-09-13
 */
@RestController
@RequestMapping("/api/base/role-permission")
public class BaseRolePermissionController {

    @Resource
    private IBaseRolePermissionService baseRolePermissionService;

    @PostMapping("/list-query")
    public ApiResult<List<BaseRolePermissionDTO>> query(@Valid @RequestBody RolePemissionListReqVO reqVO) {
        List<BaseRolePermissionDTO> r = baseRolePermissionService.listByRoleIDAndParentId(reqVO.getRoleId(), BaseConstants.ROOT_MENU_ID);
        return success(r);
    }

    @PostMapping("/turn-on")
    public ApiResult<String> turnOn(@Valid @RequestBody RolePemissionSwitchReqVO reqVO) {
        baseRolePermissionService.turnOn(reqVO);
        return success();
    }

    @PostMapping("/turn-off")
    public ApiResult<String> turnOff(@Valid @RequestBody RolePemissionSwitchReqVO reqVO) {
        baseRolePermissionService.turnOff(reqVO);
        return success();
    }
}
