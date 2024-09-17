package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.menu.MenuQueryReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseMenuDO;
import cn.yessoft.umsj.moduler.base.entity.BaseRolePermissionDO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseMenuDTO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseRolePermissionDTO;
import cn.yessoft.umsj.moduler.base.enums.MenuTpyeEnum;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import cn.yessoft.umsj.mybatis.core.query.MPJLambdaWrapperX;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单及功能表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-08-31
 */
public interface BaseMenuMapper extends YesBaseMapper<BaseMenuDO> {

    default PageResult<BaseMenuDTO> selectPage(MenuQueryReqVO reqVO) {
        MPJLambdaWrapperX<BaseMenuDO> query = new MPJLambdaWrapperX<BaseMenuDO>();
        query.selectAll(BaseMenuDO.class);
        query.selectAs("parentmenu.name", BaseMenuDTO::getParent);
        query.leftJoin(BaseMenuDO.class, "parentmenu", BaseMenuDO::getId, BaseMenuDO::getParentId);
        query.eqIfPresent(BaseMenuDO::getType, reqVO.getType());
        query.likeIfExists(BaseMenuDO::getName, reqVO.getName()).or().likeIfExists("parentmenu.name", reqVO.getName());
        return selectJoinPage(reqVO, BaseMenuDTO.class, query);
    }

    default List<BaseRolePermissionDTO> listByRoleIDAndParentId(Long roleId, Long parentId) {
        MPJLambdaWrapperX<BaseMenuDO> query = new MPJLambdaWrapperX<BaseMenuDO>();
        query.selectAs("t.id", BaseRolePermissionDTO::getMenuId);
        query.selectAs("t.name", BaseRolePermissionDTO::getName);
        query.selectAs("t.permission", BaseRolePermissionDTO::getPermission);
        query.selectAs("t.type", BaseRolePermissionDTO::getType);
        query.selectAs("t.locale", BaseRolePermissionDTO::getLocale);
        query.selectAs("t.icon", BaseRolePermissionDTO::getIcon);
        query.selectAs("rp.role_id", BaseRolePermissionDTO::getRoleId);
        query.leftJoin(BaseRolePermissionDO.class, "rp", on -> on.eq(BaseRolePermissionDO::getPermission, BaseMenuDO::getPermission)
                .eq(BaseRolePermissionDO::getRoleId, roleId));
        query.eq(BaseMenuDO::getParentId, parentId);
        return selectJoinList(BaseRolePermissionDTO.class, query);
    }

    default List<BaseMenuDO> listByPermissionsAndParentId(Set<String> permissions, Long rootMenuId) {
        return selectList(new LambdaQueryWrapperX<BaseMenuDO>()
                .in(BaseMenuDO::getPermission, permissions)
                .eq(BaseMenuDO::getParentId, rootMenuId)
                .eq(BaseMenuDO::getType, MenuTpyeEnum.MENU.getValue())
                .eq(BaseMenuDO::getEnable, true
                ));
    }

    ;
}
