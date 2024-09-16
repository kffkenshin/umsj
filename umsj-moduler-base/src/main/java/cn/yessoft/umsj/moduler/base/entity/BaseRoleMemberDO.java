package cn.yessoft.umsj.moduler.base.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色成员表
 * </p>
 *
 * @author ethan
 * @since 2024-09-10
 */
@Getter
@Setter
@TableName("base_role_member")
public class BaseRoleMemberDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * ID
     */
    private Long roleId;

    /**
     * 成员类型
     */
    private Integer type;

    /**
     * 成员类型ID
     */
    private Long memberId;

    /**
     * 成员名称
     */
    private String memberName;
}
