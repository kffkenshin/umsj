package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.yessoft.umsj.moduler.base.entity.BaseAccessTokenDO;
import cn.yessoft.umsj.moduler.base.entity.BaseRefreshTokenDO;
import cn.yessoft.umsj.moduler.base.mapper.BaseAccessTokenMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseAccessTokenService;
import cn.yessoft.umsj.moduler.base.service.IBaseRefreshTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * AccessToken表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-08-22
 */
@Service("baseAccessTokenService")
public class BaseAccessTokenServiceImpl extends ServiceImpl<BaseAccessTokenMapper, BaseAccessTokenDO> implements IBaseAccessTokenService {

    @Resource
    private IBaseRefreshTokenService baseRefreshTokenService;

    @Resource
    private BaseAccessTokenMapper baseAccessTokenMapper;

    @Value("${yesee.base.access-token-expire-time}")
    private int ACCESS_TOKEN_EXPIRE_TIME;

    @Override
    public BaseAccessTokenDO refreshAccessToken(String refreshToken) {
        return null;
    }

    @Override
    @Transactional
    public BaseAccessTokenDO createAccessToken(Long accountID) {
        // 创建刷新令牌
        BaseRefreshTokenDO refreshTokenDO = baseRefreshTokenService.createRefreshToken(accountID);
        // 创建访问令牌
        return createOAuth2AccessToken(refreshTokenDO);
    }

    private BaseAccessTokenDO createOAuth2AccessToken(BaseRefreshTokenDO refreshTokenDO) {
        BaseAccessTokenDO accessTokenDO = new BaseAccessTokenDO().setAccessToken(IdUtil.fastSimpleUUID())
                .setAccountId(refreshTokenDO.getAccountId())
                .setRefreshToken(refreshTokenDO.getRefreshToken())
                .setExpiresTime(LocalDateTime.now().plusSeconds(ACCESS_TOKEN_EXPIRE_TIME));
        baseAccessTokenMapper.insert(accessTokenDO);
        // todo记录到 Redis 中
        return accessTokenDO;
    }

    @Override
    public BaseAccessTokenDO removeAccessToken(String token) {
        return null;
    }

    @Override
    public BaseAccessTokenDO checkAccessToken(String token) {
        return baseAccessTokenMapper.selectOne(BaseAccessTokenDO::getAccessToken, token);
    }
}
