package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yessoft.umsj.moduler.base.controller.vo.LoginReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.LoginRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseAccessTokenDO;
import cn.yessoft.umsj.moduler.base.entity.BaseAccountDO;
import cn.yessoft.umsj.moduler.base.enums.LoginLogTypeEnum;
import cn.yessoft.umsj.moduler.base.service.IBaseAccessTokenService;
import cn.yessoft.umsj.moduler.base.service.IBaseAccountService;
import cn.yessoft.umsj.moduler.base.service.ISystemAuthService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;


/**
 * Auth Service 实现类
 */
@Service("systemAuthService")
@Slf4j
public class SystemAuthServiceImpl implements ISystemAuthService {

    @Resource
    private IBaseAccessTokenService baseAccessTokenService;

    @Resource
    private IBaseAccountService baseAccountService;


    @Override
    public BaseAccountDO authenticate(String aid, String password) {
        // 校验账号是否存在
        BaseAccountDO user = baseAccountService.getByAid(aid);
        if (user == null || user.getEnable() == null || !user.getEnable()) {
            //todu 异常登录日志
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!baseAccountService.isPasswordMatch(password, user.getPassword())) {
            //todu 异常登录日志
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        return user;
    }

    @Override
    public LoginRespVO login(LoginReqVO reqVO) {

        // 使用账号密码，进行登录
        BaseAccountDO user = authenticate(reqVO.getAid(), reqVO.getPassword());

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    private LoginRespVO createTokenAfterLoginSuccess(Long accountID, LoginLogTypeEnum logType) {
        //todu 登录日志
        // 创建访问令牌
        BaseAccessTokenDO baseAccessToken = baseAccessTokenService.createAccessToken(accountID);
        // 构建返回结果
        return BeanUtil.copyProperties(baseAccessToken, LoginRespVO.class);
    }

    @Override
    public LoginRespVO refreshToken(String refreshToken) {
        BaseAccessTokenDO baseAccessToken = baseAccessTokenService.refreshAccessToken(refreshToken);
        return BeanUtil.copyProperties(baseAccessToken, LoginRespVO.class);
    }


    @Override
    public void logout(String token) {
        // 删除访问令牌
        BaseAccessTokenDO baseAccessToken = baseAccessTokenService.removeAccessToken(token);
        if (baseAccessToken == null) {
            return;
        }
        // 删除成功，则记录登出日志
        //todu 登录日志
    }
}
