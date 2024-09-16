package cn.yessoft.umsj.moduler.base.controller.vo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateUserInfoReqVO {

    @NotEmpty(message = "邮箱不能为空")
    private String email;

    @NotEmpty(message = "昵称不能为空")
    private String nickName;
}