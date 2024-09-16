package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.moduler.base.entity.BaseFileConfigDO;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClient;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文件配置表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-07
 */
public interface IBaseFileConfigService extends IService<BaseFileConfigDO> {

    FileClient getMasterFileClient();

    FileClient getFileClient(Long configId);
}
