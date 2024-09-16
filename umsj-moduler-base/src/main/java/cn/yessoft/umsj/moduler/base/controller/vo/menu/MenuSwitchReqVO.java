package cn.yessoft.umsj.moduler.base.controller.vo.menu;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MenuSwitchReqVO {
    @NotBlank(message = "ID不能为空")
    private String id;
}
