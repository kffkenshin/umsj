package cn.yessoft.umsj.moduler.base.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserInfoRespVO {

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

    private LocalDateTime createTime;

    private Set<String> permissions;

}
