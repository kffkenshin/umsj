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
 * 客户物料表
 * </p>
 *
 * @author ethan
 * @since 2024-09-24
 */
@Getter
@Setter
@TableName("xhf_customer_item")
public class XhfCustomerItemDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 厂内料号ID
     */
    private Long itemId;

    /**
     * 客户料号编号
     */
    private String customerItemNo;

    /**
     * 客户ID
     */
    private Long cusomerId;

    /**
     * 客户料号名称
     */
    private String customerItemName;

    /**
     * 客户料号规格
     */
    private String customerItemSpec;
}
