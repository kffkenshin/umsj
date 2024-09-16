package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.moduler.base.controller.vo.LoginReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.LoginRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseAccountDO;
import jakarta.validation.Valid;

/**
 * 管理后台的认证 Service 接口
 * <p>
 * 提供用户的登录、登出的能力
 */
public interface ISystemAuthService {
    /**
     * 验证账号 + 密码。如果通过，则返回用户
     *
     * @param username 账号
     * @param password 密码
     * @return 用户
     */
    BaseAccountDO authenticate(String username, String password);

    /**
     * 账号登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    LoginRespVO login(@Valid LoginReqVO reqVO);

    /**
     * 基于 token 退出登录
     *
     * @param token token
     */
    void logout(String token);


    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 登录结果
     */
    LoginRespVO refreshToken(String refreshToken);

}
