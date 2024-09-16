package cn.yessoft.umsj.moduler.base.framework.file.core.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClient;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClientConfig;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.local.LocalFileClient;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.local.LocalFileClientConfig;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.s3.S3FileClient;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.s3.S3FileClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件存储器枚举
 *
 * @author 芋道源码
 */
@AllArgsConstructor
@Getter
public enum FileStorageEnum {


    LOCAL(10, LocalFileClientConfig.class, LocalFileClient.class),

    S3(20, S3FileClientConfig.class, S3FileClient.class),
    ;

    /**
     * 存储器
     */
    private final Integer storage;

    /**
     * 配置类
     */
    private final Class<? extends FileClientConfig> configClass;
    /**
     * 客户端类
     */
    private final Class<? extends FileClient> clientClass;

    public static FileStorageEnum getByStorage(Integer storage) {
        return ArrayUtil.firstMatch(o -> o.getStorage().equals(storage), values());
    }

}
