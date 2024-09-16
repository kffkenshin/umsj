package cn.yessoft.umsj.moduler.base.controller.vo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePwdReqVO {

    @NotEmpty(message = "旧密码不能为空")
    @Length(min = 4, max = 16, message = "账号长度为 4-16 位")
    private String oldPassword;

    @NotEmpty(message = "新密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 6-16 位")
    private String password;
}