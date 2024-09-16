package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.yessoft.umsj.moduler.base.entity.BaseRefreshTokenDO;
import cn.yessoft.umsj.moduler.base.mapper.BaseRefreshTokenMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseRefreshTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * RefreshToken表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-08-23
 */
@Service("baseRefreshTokenService")
public class BaseRefreshTokenServiceImpl extends ServiceImpl<BaseRefreshTokenMapper, BaseRefreshTokenDO> implements IBaseRefreshTokenService {

    @Resource
    private BaseRefreshTokenMapper baseRefreshTokenMapper;
    @Value("${yesee.base.refresh-token-expire-time}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    @Override
    @Transactional
    public BaseRefreshTokenDO createRefreshToken(Long accountID) {
        BaseRefreshTokenDO refreshToken = new BaseRefreshTokenDO().setRefreshToken(IdUtil.fastSimpleUUID())
                .setAccountId(accountID)
                .setExpiresTime(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRE_TIME));
        baseRefreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }
}
