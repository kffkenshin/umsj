package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.moduler.base.entity.BaseRolePermissionDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-09-13
 */
public interface BaseRolePermissionMapper extends YesBaseMapper<BaseRolePermissionDO> {

    default List<String> getPermissionsByRoleIds(Set<Long> roleIds) {
        LambdaQueryWrapperX<BaseRolePermissionDO> q = new LambdaQueryWrapperX<BaseRolePermissionDO>();
        q.select(BaseRolePermissionDO::getPermission);
        q.in(BaseRolePermissionDO::getRoleId, roleIds);
        return selectObjs(q);
    }
}
