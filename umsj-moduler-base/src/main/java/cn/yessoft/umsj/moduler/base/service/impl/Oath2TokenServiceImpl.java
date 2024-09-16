package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yessoft.umsj.moduler.base.service.IBaseAccessTokenService;
import cn.yessoft.umsj.moduler.base.service.IBaseRolePermissionService;
import cn.yessoft.umsj.moduler.base.service.IBaseRoleService;
import cn.yessoft.umsj.security.entity.dto.BaseAccessTokenDTO;
import cn.yessoft.umsj.security.service.IOath2TokenService;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * AccessToken表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-08-22
 */
@Service("othan2TokenService")
public class Oath2TokenServiceImpl implements IOath2TokenService {

    @Resource
    private IBaseAccessTokenService baseAccessTokenService;

    @Resource
    private IBaseRoleService baseRoleService;

    @Resource
    private IBaseRolePermissionService baseRolePermissionService;


    @Override
    public BaseAccessTokenDTO checkAccessToken(String token) {
        return BeanUtil.toBean(baseAccessTokenService.checkAccessToken(token), BaseAccessTokenDTO.class);
    }

    @Override
    public Set<String> getPermissions(Long accountID) {
        Set<Long> roleIds = baseRoleService.getAllByAccountID(accountID);
        return Sets.newHashSet(baseRolePermissionService.getPermissionsByRoleIds(roleIds));
    }

    @Override
    public Set<String> getRoleNos(Long accountID) {
        Set<Long> roleIds = baseRoleService.getAllByAccountID(accountID);
        return Sets.newHashSet(baseRoleService.getRoleNosByIds(roleIds));
    }
}
