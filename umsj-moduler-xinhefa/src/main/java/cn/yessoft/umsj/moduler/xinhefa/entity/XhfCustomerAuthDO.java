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
 * 客服权限表
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
@Getter
@Setter
@TableName("xhf_customer_auth")
public class XhfCustomerAuthDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 负责账号ID
     */
    private Long accountId;

    /**
     * 产品类型
     */
    private String itemTypeIds;

    /**
     * 角色ID
     */
    private Long roleId;
}
