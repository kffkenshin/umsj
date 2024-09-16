package cn.yessoft.umsj.moduler.base.controller.vo.rolepermission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RolePemissionListReqVO {

    @NotNull(message = "角色不能为空")
    private Long roleId;

}
