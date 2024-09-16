package cn.yessoft.umsj.security.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * AccessToken表
 * </p>
 *
 * @author ethan
 * @since 2024-08-22
 */
@Data
public class BaseAccessTokenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 用户ID
     */
    private Long accountId;

    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;
}
