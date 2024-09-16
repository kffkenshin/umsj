package cn.yessoft.umsj.security.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.yessoft.umsj.common.exception.ServiceException;
import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.utils.ServletUtils;
import cn.yessoft.umsj.security.config.SecurityProperties;
import cn.yessoft.umsj.security.core.LoginUser;
import cn.yessoft.umsj.security.core.util.SecurityFrameworkUtils;
import cn.yessoft.umsj.security.entity.dto.BaseAccessTokenDTO;
import cn.yessoft.umsj.security.service.IOath2TokenService;
import cn.yessoft.umsj.web.core.handler.GlobalExceptionHandler;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Token 过滤器，验证 token 的有效性
 * 验证通过后，获得 {@link LoginUser} 信息，并加入到 Spring Security 上下文
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final GlobalExceptionHandler globalExceptionHandler;

    private final IOath2TokenService othan2TokenService;

    // todo 暂时用 要换redis
    private static final Cache<String, Object> cache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();


    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotEmpty(token)) {
            try {
                // 1.1 基于 token 构建登录用户
                LoginUser loginUser = (LoginUser) cache.getIfPresent("token");
                if (loginUser == null) {
                    loginUser = buildLoginUserByToken(token);
                    cache.put("token", loginUser);
                }
                // 2. 设置当前用户
                if (loginUser != null) {
                    SecurityFrameworkUtils.setLoginUser(loginUser, request);
                }
            } catch (Throwable ex) {
                ApiResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
                ServletUtils.writeJSON(response, result);
                return;
            }
        }
        // 继续过滤链
        chain.doFilter(request, response);
    }

    private LoginUser buildLoginUserByToken(String token) {
        try {
            BaseAccessTokenDTO tokenDTO = othan2TokenService.checkAccessToken(token);
            if (tokenDTO == null) {
                return null;
            }
            Set<String> permissions = othan2TokenService.getPermissions(tokenDTO.getAccountId());
            // 构建登录用户
            return new LoginUser().setId(tokenDTO.getAccountId()).setExpiresTime(tokenDTO.getExpiresTime()).setPermissions(permissions);
        } catch (ServiceException serviceException) {
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            return null;
        }
    }

}
