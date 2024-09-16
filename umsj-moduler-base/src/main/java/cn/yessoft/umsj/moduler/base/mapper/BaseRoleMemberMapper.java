package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.moduler.base.entity.BaseRoleMemberDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;

import java.util.List;

/**
 * <p>
 * 角色成员表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-09-10
 */
public interface BaseRoleMemberMapper extends YesBaseMapper<BaseRoleMemberDO> {

    default List<Long> getRoleIdByMemberIdAndType(Long accountID, Integer roleType) {
        LambdaQueryWrapperX<BaseRoleMemberDO> q = new LambdaQueryWrapperX<BaseRoleMemberDO>();
        q.select(BaseRoleMemberDO::getRoleId);
        q.eq(BaseRoleMemberDO::getMemberId, accountID);
        q.eq(BaseRoleMemberDO::getType, roleType);
        return selectObjs(q);
    }

    ;
}
