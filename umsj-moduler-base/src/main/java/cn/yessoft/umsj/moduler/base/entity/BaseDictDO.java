package cn.yessoft.umsj.moduler.base.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
@Getter
@Setter
@TableName("base_dict")
public class BaseDictDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字典名字
     */
    private String name;

    /**
     * 说明
     */
    private String description;
}
