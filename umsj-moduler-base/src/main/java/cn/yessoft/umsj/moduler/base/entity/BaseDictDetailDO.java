package cn.yessoft.umsj.moduler.base.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数据字典明细表
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
@Getter
@Setter
@TableName("base_dict_detail")
public class BaseDictDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字典id
     */
    private Long dictId;

    /**
     * 顺序号
     */
    private Byte seq;

    /**
     * 明细编号
     */
    private String detailKey;

    /**
     * 明细名称
     */
    private String detailValue;
}
