package cn.yessoft.umsj.moduler.base.controller.vo.account;

import lombok.Data;

/**
 * <p>
 * 用户账户
 * </p>
 *
 * @author ethan
 * @since 2024-09-04
 */
@Data
public class BaseAccountRespVO {


    /**
     * ID
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

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
