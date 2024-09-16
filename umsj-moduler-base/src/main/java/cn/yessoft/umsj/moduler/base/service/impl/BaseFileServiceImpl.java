package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.yessoft.umsj.common.utils.FileUtils;
import cn.yessoft.umsj.moduler.base.entity.BaseFileDO;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClient;
import cn.yessoft.umsj.moduler.base.framework.file.core.utils.FileTypeUtils;
import cn.yessoft.umsj.moduler.base.mapper.BaseFileMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseFileConfigService;
import cn.yessoft.umsj.moduler.base.service.IBaseFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-07
 */
@Service("baseFileService")
public class BaseFileServiceImpl extends ServiceImpl<BaseFileMapper, BaseFileDO> implements IBaseFileService {

    @Resource
    private IBaseFileConfigService baseFileConfigService;

    @Resource
    private BaseFileMapper baseFileMapper;

    @Override
    @SneakyThrows
    public <T> String createFile(byte[] content, Class<T> o, Long refId, String name, String path) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = baseFileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        BaseFileDO file = new BaseFileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        if (o != null) {
            file.setRefTable(o.getName());
        }
        if (refId != null) {
            file.setRefTableId(refId);
        }
        baseFileMapper.insert(file);
        return url;
    }

    @Override
    public <T> String createFile(byte[] bytes, Class<T> o, Long id) {
        return createFile(bytes, o, id, null, null);
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = baseFileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }
}
