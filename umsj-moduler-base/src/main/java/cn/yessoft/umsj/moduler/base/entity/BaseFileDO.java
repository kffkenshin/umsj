package cn.yessoft.umsj.moduler.base.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author ethan
 * @since 2024-09-07
 */
@Getter
@Setter
@TableName("base_file")
public class BaseFileDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置ID
     */
    private Long configId;

    /**
     * 文件名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * url
     */
    private String url;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 引用表
     */
    private String refTable;

    /**
     * 引用表ID
     */
    private Long refTableId;
}
