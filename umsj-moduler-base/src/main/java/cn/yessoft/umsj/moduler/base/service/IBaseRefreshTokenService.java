package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.moduler.base.entity.BaseRefreshTokenDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * RefreshToken表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-08-23
 */
public interface IBaseRefreshTokenService extends IService<BaseRefreshTokenDO> {

    BaseRefreshTokenDO createRefreshToken(Long accountID);
}
