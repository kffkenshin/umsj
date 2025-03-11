package cn.yessoft.umsj.moduler.base.service.impl;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.DATA_NOT_EXISTS;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.menu.MenuQueryReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseMenuDO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseMenuDTO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseRolePermissionDTO;
import cn.yessoft.umsj.moduler.base.entity.dto.UserMenuDTO;
import cn.yessoft.umsj.moduler.base.entity.dto.UserMenuMetaDTO;
import cn.yessoft.umsj.moduler.base.mapper.BaseMenuMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 菜单及功能表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-08-31
 */
@Service("baseMenuService")
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenuDO> implements IBaseMenuService {
    @Resource
    private BaseMenuMapper baseMenuMapper;

    @Override
    public PageResult<BaseMenuDTO> pagedQuery(MenuQueryReqVO reqVO) {
        return baseMenuMapper.selectPage(reqVO);
    }

    @Override
    public BaseMenuDO getMenu(Long id) {
        return baseMenuMapper.selectById(id);
    }

    @Override
    @Transactional
    public void switchEnable(String id) {
        BaseMenuDO menu = baseMenuMapper.selectById(id);
        if (menu == null) {
            throw exception(DATA_NOT_EXISTS);
        }
        menu.setEnable(menu.getEnable() == 1 ? 0 : 1);
        baseMenuMapper.updateById(menu);
    }

    @Override
    public List<BaseRolePermissionDTO> listByRoleIDAndParentId(Long roleId, Long parentId) {
        return baseMenuMapper.listByRoleIDAndParentId(roleId, parentId);
    }

    @Override
    public List<UserMenuDTO> getMenuByPermissions(Set<String> permissions, Long rootMenuId) {
        List<BaseMenuDO> menus = baseMenuMapper.listByPermissionsAndParentId(permissions, rootMenuId);
        List<UserMenuDTO> results = BeanUtils.toBean(menus, UserMenuDTO.class);
        results.forEach(i -> {
            BaseMenuDO e = menus.stream().filter(j -> j.getId().equals(i.getId())).findFirst().orElse(null);
            UserMenuMetaDTO meta = BeanUtils.toBean(e, UserMenuMetaDTO.class);
            meta.setOrder(e.getSeq());
            i.setMeta(meta);
            List<UserMenuDTO> children = getMenuByPermissions(permissions, i.getId());
            i.setChildren(children.isEmpty() ? null : children);
        });
        return results;
    }
}
