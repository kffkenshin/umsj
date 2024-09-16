package cn.yessoft.umsj.moduler.base.entity;

import cn.yessoft.umsj.common.utils.json.JsonUtils;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.FileClientConfig;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.local.LocalFileClientConfig;
import cn.yessoft.umsj.moduler.base.framework.file.core.client.s3.S3FileClientConfig;
import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;

import java.lang.reflect.Field;

/**
 * <p>
 * 文件配置表
 * </p>
 *
 * @author ethan
 * @since 2024-09-07
 */
@TableName(value = "base_file_config", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseFileConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 存储器
     */
    private Integer storage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否主数据
     */
    private Boolean master;

    /**
     * 配置信息
     */
    @TableField(typeHandler = FileClientConfigTypeHandler.class)
    private FileClientConfig config;

    public static class FileClientConfigTypeHandler extends AbstractJsonTypeHandler<Object> {

        public FileClientConfigTypeHandler(Class<?> type) {
            super(type);
        }

        public FileClientConfigTypeHandler(Class<?> type, Field field) {
            super(type, field);
        }

        @Override
        public Object parse(String json) {
            FileClientConfig config = JsonUtils.parseObjectQuietly(json, new TypeReference<>() {
            });
            if (config != null) {
                return config;
            }

            Integer storage = JsonUtils.parseObject(json, "storage", Integer.class);
            switch (storage) {
                case 10:
                    return JsonUtils.parseObject2(json, LocalFileClientConfig.class);
                case 20:
                    return JsonUtils.parseObject2(json, S3FileClientConfig.class);
                default:
                    throw new IllegalArgumentException("未知的 FileClientConfig 类型：" + json);
            }
        }

        @Override
        public String toJson(Object obj) {
            return JsonUtils.toJsonString(obj);
        }

    }
}
