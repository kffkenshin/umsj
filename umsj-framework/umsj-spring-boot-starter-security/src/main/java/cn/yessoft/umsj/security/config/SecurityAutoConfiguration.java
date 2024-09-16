package cn.yessoft.umsj.security.config;

import cn.yessoft.umsj.security.core.filter.TokenAuthenticationFilter;
import cn.yessoft.umsj.security.core.handler.AccessDeniedHandlerImpl;
import cn.yessoft.umsj.security.core.handler.AuthenticationEntryPointImpl;
import cn.yessoft.umsj.security.service.IOath2TokenService;
import cn.yessoft.umsj.security.service.ISecurityFrameworkService;
import cn.yessoft.umsj.security.service.SecurityFrameworkServiceImpl;
import cn.yessoft.umsj.web.core.handler.GlobalExceptionHandler;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Spring Security 自动配置类，主要用于相关组件的配置
 */
@AutoConfiguration
@AutoConfigureOrder(-1) // 目的：先于 Spring Security 自动配置，避免一键改包后，org.* 基础包无法生效
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    @Lazy
    private IOath2TokenService othan2TokenService;

    /**
     * 认证失败处理类 Bean
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    /**
     * 权限不够处理器 Bean
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    /**
     * Spring Security 加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityProperties.getPasswordEncoderLength());
    }

    /**
     * Token 认证过滤器 Bean
     */
    @Bean
    public TokenAuthenticationFilter authenticationTokenFilter(GlobalExceptionHandler globalExceptionHandler) {
        return new TokenAuthenticationFilter(securityProperties, globalExceptionHandler, othan2TokenService);
    }

    @Bean("yes") // 使用 Spring Security 的缩写，方便使用
    public ISecurityFrameworkService securityFrameworkService() {
        return new SecurityFrameworkServiceImpl();
    }
}
