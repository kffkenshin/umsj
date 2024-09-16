package cn.yessoft.umsj.moduler.base.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户账户
 * </p>
 *
 * @author ethan
 * @since 2024-09-04
 */
@Getter
@Setter
@TableName("base_account")
public class BaseAccountDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 登录账号
     */
    private String aid;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 手机号码
     */
    private String cellPhone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 启用停用
     */
    private Boolean enable;
}
