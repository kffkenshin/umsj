package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.moduler.base.entity.BaseFileDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文件表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-07
 */
public interface IBaseFileService extends IService<BaseFileDO> {

    <T> String createFile(byte[] content, Class<T> targetType, Long id, String name, String path);

    <T> String createFile(byte[] bytes, Class<T> targetType, Long id);

    byte[] getFileContent(Long configId, String path) throws Exception;
}
