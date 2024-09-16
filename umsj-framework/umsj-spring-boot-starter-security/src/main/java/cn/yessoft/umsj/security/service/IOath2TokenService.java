package cn.yessoft.umsj.security.service;

import cn.yessoft.umsj.security.entity.dto.BaseAccessTokenDTO;

import java.util.Set;

/**
 * <p>
 * Oath2Token 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-08-22
 */
public interface IOath2TokenService {

    BaseAccessTokenDTO checkAccessToken(String token);

    Set<String> getPermissions(Long accountID);

    Set<String> getRoleNos(Long accountID);
}
