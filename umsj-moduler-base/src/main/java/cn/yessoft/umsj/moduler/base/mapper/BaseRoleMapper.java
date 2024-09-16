package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.moduler.base.entity.BaseRoleDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-09-10
 */
public interface BaseRoleMapper extends YesBaseMapper<BaseRoleDO> {

    default List<String> getRoleNosByIds(Set<Long> roleIds) {
        LambdaQueryWrapperX<BaseRoleDO> q = new LambdaQueryWrapperX<BaseRoleDO>();
        q.select(BaseRoleDO::getRoleNo);
        q.in(BaseRoleDO::getId, roleIds);
        return selectObjs(q);
    }

    ;
}
