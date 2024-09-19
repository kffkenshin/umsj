package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleMemberCreateVO;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleQueryReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseRoleMemberDO;
import cn.yessoft.umsj.moduler.base.entity.dto.IdAndNameDTO;
import cn.yessoft.umsj.moduler.base.enums.RoleTpyeEnum;
import cn.yessoft.umsj.moduler.base.mapper.BaseRoleMemberMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseRoleMemberService;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_DUPLICATE;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

/**
 * <p>
 * 角色成员表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-10
 */
@Service("baseRoleMemberService")
public class BaseRoleMemberServiceImpl extends ServiceImpl<BaseRoleMemberMapper, BaseRoleMemberDO> implements IBaseRoleMemberService {
    @Resource
    private BaseRoleMemberMapper baseRoleMemberMapper;

    @Override
    public PageResult<BaseRoleMemberDO> pagedQuery(RoleQueryReqVO reqVO) {
        return baseRoleMemberMapper.selectPage(reqVO, new LambdaQueryWrapperX<BaseRoleMemberDO>()
                .eq(BaseRoleMemberDO::getRoleId, reqVO.getRoleId()));
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        baseRoleMemberMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public void deleteByRoleId(Long i) {
        baseRoleMemberMapper.delete(BaseRoleMemberDO::getRoleId, i);
    }

    @Override
    public void create(RoleMemberCreateVO reqVO) {
        // 验证唯一性
        validateUnique(reqVO.getRoleId(), reqVO.getMemberId(), reqVO.getType());
        BaseRoleMemberDO r = BeanUtil.toBean(reqVO, BaseRoleMemberDO.class);
        baseRoleMemberMapper.insert(r);

    }

    private void validateUnique(String roleId, String memberId, Integer type) {
        BaseRoleMemberDO entity = baseRoleMemberMapper.selectOne(BaseRoleMemberDO::getRoleId, roleId,
                BaseRoleMemberDO::getMemberId, memberId, BaseRoleMemberDO::getType, type);
        if (entity != null) {
            throw exception(O_DUPLICATE, "成员");
        }
    }

    @Override
    public BaseRoleMemberDO validateExist(Long id) {
        BaseRoleMemberDO entity = baseRoleMemberMapper.selectById(id);
        if (BeanUtil.isEmpty(entity)) {
            throw exception(O_NOT_EXISTS, "角色");
        }
        return entity;
    }

    @Override
    public List<Long> getRoleIdByAccountId(Long accountID) {
        return baseRoleMemberMapper.getRoleIdByMemberIdAndType(accountID, RoleTpyeEnum.ACCOUNT.getValue());
    }

    @Override
    public Set<Long> getAllByRoleId(Long roleId, Set<Long> selectedIds) {
        List<Long> ids = baseRoleMemberMapper.getRoleIdByMemberIdAndType(roleId, RoleTpyeEnum.ROLE.getValue());
        ids.forEach(i -> {
            if (!selectedIds.contains(i)) {
                Set<Long> _ids = getAllByRoleId(i, selectedIds);
                selectedIds.addAll(_ids);
            }
        });
        return selectedIds;
    }

    @Override
    public List<IdAndNameDTO> listRoleAccount(Long roleId) {
        return baseRoleMemberMapper.listRoleAccount(roleId);
    }
}
