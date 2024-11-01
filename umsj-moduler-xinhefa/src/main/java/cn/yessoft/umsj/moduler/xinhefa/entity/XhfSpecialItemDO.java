package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 特殊料号
 * </p>
 *
 * @author ethan
 * @since 2024-10-19
 */
@Getter
@Setter
@TableName("xhf_special_item")
public class XhfSpecialItemDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 料号ID
     */
    private Long itemId;

    /**
     * 备注
     */
    private String remark;
}
