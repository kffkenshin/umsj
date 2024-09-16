package cn.yessoft.umsj.moduler.base.service.impl;

import cn.yessoft.umsj.moduler.base.controller.vo.rolepermission.RolePemissionSwitchReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseMenuDO;
import cn.yessoft.umsj.moduler.base.entity.BaseRolePermissionDO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseRolePermissionDTO;
import cn.yessoft.umsj.moduler.base.mapper.BaseRolePermissionMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseMenuService;
import cn.yessoft.umsj.moduler.base.service.IBaseRolePermissionService;
import cn.yessoft.umsj.moduler.base.service.IBaseRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-13
 */
@Service("baseRolePermissionService")
public class BaseRolePermissionServiceImpl extends ServiceImpl<BaseRolePermissionMapper, BaseRolePermissionDO> implements IBaseRolePermissionService {
    @Resource
    private BaseRolePermissionMapper baseRolePermissionMapper;

    @Resource
    private IBaseMenuService baseMenuService;

    @Resource
    private IBaseRoleService baseRoleService;

    @Override
    public void turnOn(RolePemissionSwitchReqVO reqVO) {
        // 验证菜单和角色
        BaseMenuDO menu = validateExist(reqVO);
        BaseRolePermissionDO r = baseRolePermissionMapper.selectOne(BaseRolePermissionDO::getRoleId, reqVO.getRoleId(), BaseRolePermissionDO::getPermission, menu.getPermission());
        if (r != null) {
            return;
        }
        r = new BaseRolePermissionDO();
        r.setRoleId(reqVO.getRoleId());
        r.setPermission(menu.getPermission());
        baseRolePermissionMapper.insert(r);
    }

    @Override
    public void turnOff(RolePemissionSwitchReqVO reqVO) {
        // 验证菜单和角色
        BaseMenuDO menu = validateExist(reqVO);
        BaseRolePermissionDO r = baseRolePermissionMapper.selectOne(BaseRolePermissionDO::getRoleId, reqVO.getRoleId(), BaseRolePermissionDO::getPermission, menu.getPermission());
        if (r == null) {
            return;
        }
        baseRolePermissionMapper.deleteById(r);
    }

    private BaseMenuDO validateExist(RolePemissionSwitchReqVO reqVO) {
        // 验证菜单
        BaseMenuDO menu = baseMenuService.getMenu(reqVO.getMenuId());
        if (menu == null) {
            throw exception(O_NOT_EXISTS, "菜单");
        }
        // 验证角色
        baseRoleService.validateExist(reqVO.getRoleId());
        return menu;
    }

    @Override
    public List<BaseRolePermissionDTO> listByRoleIDAndParentId(Long roleId, Long parentId) {
        List<BaseRolePermissionDTO> r = baseMenuService.listByRoleIDAndParentId(roleId, parentId);
        r.forEach(i -> {
            List<BaseRolePermissionDTO> children = listByRoleIDAndParentId(roleId, i.getMenuId());
            i.setChildren(children);
        });
        return r;
    }

    @Override
    public List<String> getPermissionsByRoleIds(Set<Long> roleIds) {
        return baseRolePermissionMapper.getPermissionsByRoleIds(roleIds);
    }

}
