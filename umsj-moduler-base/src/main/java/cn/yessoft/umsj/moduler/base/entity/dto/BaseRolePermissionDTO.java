package cn.yessoft.umsj.moduler.base.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class BaseRolePermissionDTO {

    private Long menuId;

    /**
     * 名称
     */
    private String name;

    /**
     * 权限值
     */
    private String permission;

    /**
     * 菜单类型
     */
    private Integer type;


    /**
     * 图标
     */
    private String icon;

    private Boolean hasPermission;

    private Long roleId;

    private List<BaseRolePermissionDTO> children;

    public Boolean getHasPermission() {
        return roleId != null;
    }
}
