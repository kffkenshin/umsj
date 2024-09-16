package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleQueryReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseRoleDO;
import cn.yessoft.umsj.moduler.base.mapper.BaseRoleMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseRoleMemberService;
import cn.yessoft.umsj.moduler.base.service.IBaseRoleService;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
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
 * 角色表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-10
 */
@Service("baseRoleService")
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, BaseRoleDO> implements IBaseRoleService {

    @Resource
    private BaseRoleMapper baseRoleMapper;

    @Resource
    private IBaseRoleMemberService baseRoleMemberService;

    @Override
    public PageResult<BaseRoleDO> pagedQuery(RoleQueryReqVO reqVO) {
        return baseRoleMapper.selectPage(reqVO, new LambdaQueryWrapperX<BaseRoleDO>()
                .likeIfPresent(BaseRoleDO::getRoleNo, reqVO.getInfo())
                .or().likeIfPresent(BaseRoleDO::getRoleName, reqVO.getInfo()));
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        ids.forEach(i -> {
            BaseRoleDO baseRoleDO = baseRoleMapper.selectById(i);
            if (baseRoleDO != null && !baseRoleDO.getRetain()) {
                baseRoleMemberService.deleteByRoleId(i);
                baseRoleMapper.deleteById(i);
            }
        });
    }

    @Override
    public void create(RoleRespVO reqVO) {
        // 验证唯一性
        validateNoUnique(reqVO.getRoleNo());
        BaseRoleDO r = BeanUtil.toBean(reqVO, BaseRoleDO.class);
        r.setRetain(false);
        baseRoleMapper.insert(r);
    }

    private void validateNoUnique(String roleNo) {
        BaseRoleDO entity = getByNo(roleNo);
        if (entity != null) {
            throw exception(O_DUPLICATE, "编号");
        }
    }

    @Override
    public BaseRoleDO getByNo(String roleNo) {
        return baseRoleMapper.selectOne(BaseRoleDO::getRoleNo, roleNo);
    }

    @Override
    public void update(RoleRespVO reqVO) {
        //校验存在
        BaseRoleDO entity = validateExist(reqVO.getId());
        entity.setRoleNo(reqVO.getRoleNo());
        entity.setRoleName(reqVO.getRoleName());
        baseRoleMapper.updateById(entity);
    }

    @Override
    public List<BaseRoleDO> listQuery(RoleQueryReqVO reqVO) {
        return baseRoleMapper.selectList(new Page<>(0, 100), new LambdaQueryWrapperX<BaseRoleDO>()
                .likeIfPresent(BaseRoleDO::getRoleNo, reqVO.getInfo())
                .or().likeIfPresent(BaseRoleDO::getRoleName, reqVO.getInfo()));
    }

    @Override
    public Set<Long> getAllByAccountID(Long accountID) {
        Set<Long> r = Sets.newHashSet();
        List<Long> arids = baseRoleMemberService.getRoleIdByAccountId(accountID);
        r.addAll(arids);
        arids.forEach(i -> {
            Set<Long> rids = baseRoleMemberService.getAllByRoleId(i, Sets.newHashSet());
            r.addAll(rids);
        });
        return r;
    }

    @Override
    public List<String> getRoleNosByIds(Set<Long> roleIds) {
        return baseRoleMapper.getRoleNosByIds(roleIds);
    }

    @Override
    public BaseRoleDO validateExist(Long id) {
        BaseRoleDO entity = baseRoleMapper.selectById(id);
        if (BeanUtil.isEmpty(entity)) {
            throw exception(O_NOT_EXISTS, "角色");
        }
        return entity;
    }
}
