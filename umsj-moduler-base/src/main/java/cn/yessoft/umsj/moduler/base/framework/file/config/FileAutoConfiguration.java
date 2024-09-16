package cn.yessoft.umsj.moduler.base.framework.file.config;

import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClientFactory;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;

/**
 * 文件配置类
 */
public class FileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
