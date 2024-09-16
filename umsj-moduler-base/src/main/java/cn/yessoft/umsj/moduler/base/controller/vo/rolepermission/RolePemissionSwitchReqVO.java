package cn.yessoft.umsj.moduler.base.controller.vo.rolepermission;

import cn.yessoft.umsj.common.pojo.PageParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RolePemissionSwitchReqVO extends PageParam {

    @NotNull(message = "角色不能为空")
    private Long roleId;

    @NotNull(message = "权限不能为空")
    private Long menuId;

}
