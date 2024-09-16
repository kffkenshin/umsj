package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.moduler.base.entity.BaseAccessTokenDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * AccessToken表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-08-22
 */
public interface IBaseAccessTokenService extends IService<BaseAccessTokenDO> {

    BaseAccessTokenDO refreshAccessToken(String refreshToken);

    BaseAccessTokenDO createAccessToken(Long accountID);

    BaseAccessTokenDO removeAccessToken(String token);

    BaseAccessTokenDO checkAccessToken(String token);
}
