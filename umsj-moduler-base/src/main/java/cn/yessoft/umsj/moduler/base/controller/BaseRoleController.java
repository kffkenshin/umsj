package cn.yessoft.umsj.moduler.base.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleMemberCreateVO;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleMemberRespVO;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleQueryReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseRoleDO;
import cn.yessoft.umsj.moduler.base.entity.BaseRoleMemberDO;
import cn.yessoft.umsj.moduler.base.service.IBaseRoleMemberService;
import cn.yessoft.umsj.moduler.base.service.IBaseRoleService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author ethan
 * @since 2024-09-10
 */
@RestController
@RequestMapping("/api/base/role")
public class BaseRoleController {

    @Resource
    private IBaseRoleMemberService baseRoleMemberService;

    @Resource
    private IBaseRoleService baseRoleService;

    @PostMapping("/paged-query")
    public ApiResult<PageResult<RoleRespVO>> query(@Valid @RequestBody RoleQueryReqVO reqVO) {
        PageResult<BaseRoleDO> pageResult = baseRoleService.pagedQuery(reqVO);
        return success(new PageResult<>(BeanUtils.toBean(pageResult.getList(), RoleRespVO.class),
                pageResult.getTotal()));
    }

    @PostMapping("/list-query")
    public ApiResult<List<RoleRespVO>> queryList(@Valid @RequestBody RoleQueryReqVO reqVO) {
        List<BaseRoleDO> r = baseRoleService.listQuery(reqVO);
        return success(BeanUtils.toBean(r, RoleRespVO.class));
    }

    @PostMapping("/member/paged-query")
    public ApiResult<PageResult<RoleMemberRespVO>> queryMember(@Valid @RequestBody RoleQueryReqVO reqVO) {
        PageResult<BaseRoleMemberDO> pageResult = baseRoleMemberService.pagedQuery(reqVO);
        return success(new PageResult<>(BeanUtils.toBean(pageResult.getList(), RoleMemberRespVO.class),
                pageResult.getTotal()));
    }

    @PostMapping("/delete")
    public ApiResult<String> delete(@RequestBody List<Long> ids) {
        baseRoleService.delete(ids);
        return success();
    }

    @PostMapping("/delete-member")
    public ApiResult<String> deleteRoleMember(@RequestBody List<Long> ids) {
        baseRoleMemberService.delete(ids);
        return success();
    }

    @PostMapping("/create")
    public ApiResult<String> create(@Valid @RequestBody RoleRespVO reqVO) {
        baseRoleService.create(reqVO);
        return success();
    }

    @PostMapping("/update")
    public ApiResult<String> update(@Valid @RequestBody RoleRespVO reqVO) {
        baseRoleService.update(reqVO);
        return success();
    }

    @GetMapping()
    public ApiResult<RoleRespVO> get(@RequestParam("id") Long id) {
        BaseRoleDO r = baseRoleService.validateExist(id);
        return success(BeanUtils.toBean(r, RoleRespVO.class));
    }

    @PostMapping("/create-member")
    public ApiResult<String> createMember(@Valid @RequestBody RoleMemberCreateVO reqVO) {
        baseRoleMemberService.create(reqVO);
        return success();
    }

    @GetMapping("/member")
    public ApiResult<RoleMemberRespVO> getMember(@RequestParam("id") Long id) {
        BaseRoleMemberDO r = baseRoleMemberService.validateExist(id);
        return success(BeanUtils.toBean(r, RoleMemberRespVO.class));
    }
}
