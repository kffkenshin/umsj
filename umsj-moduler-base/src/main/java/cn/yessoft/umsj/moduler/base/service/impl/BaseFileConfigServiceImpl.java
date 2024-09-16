package cn.yessoft.umsj.moduler.base.service.impl;

import cn.yessoft.umsj.moduler.base.entity.BaseFileConfigDO;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClient;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClientFactory;
import cn.yessoft.umsj.moduler.base.mapper.BaseFileConfigMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseFileConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

import static cn.yessoft.umsj.common.utils.CacheUtils.buildAsyncReloadingCache;

/**
 * <p>
 * 文件配置表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-07
 */
@Service("baseFileConfigService")
public class BaseFileConfigServiceImpl extends ServiceImpl<BaseFileConfigMapper, BaseFileConfigDO> implements IBaseFileConfigService {

    private static final Long CACHE_MASTER_ID = 0L;

    /**
     * {@link FileClient} 缓存，通过它异步刷新 fileClientFactory
     */
    @Getter
    private final LoadingCache<Long, FileClient> clientCache = buildAsyncReloadingCache(Duration.ofSeconds(10L),
            new CacheLoader<Long, FileClient>() {

                @Override
                public FileClient load(Long id) {
                    BaseFileConfigDO config = Objects.equals(CACHE_MASTER_ID, id) ?
                            baseFileConfigMapper.selectByMaster() : baseFileConfigMapper.selectById(id);
                    if (config != null) {
                        fileClientFactoryImpl.createOrUpdateFileClient(config.getId(), config.getStorage(), config.getConfig());
                    }
                    return fileClientFactoryImpl.getFileClient(null == config ? id : config.getId());
                }

            });

    @Resource
    private FileClientFactory fileClientFactoryImpl;

    @Resource
    private BaseFileConfigMapper baseFileConfigMapper;

    @Resource
    private Validator validator;

    @Override
    public FileClient getMasterFileClient() {
        return clientCache.getUnchecked(CACHE_MASTER_ID);
    }

    @Override
    public FileClient getFileClient(Long configId) {
        return clientCache.getUnchecked(configId);
    }
}
