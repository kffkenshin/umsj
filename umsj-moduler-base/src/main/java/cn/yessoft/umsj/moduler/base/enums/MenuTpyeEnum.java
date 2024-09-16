package cn.yessoft.umsj.moduler.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型
 */
@Getter
@AllArgsConstructor
public enum MenuTpyeEnum {
    MENU(1, "菜单"),
    FUNCTION(2, "功能");
    /**
     * 类型
     */
    private final Integer value;
    /**
     * 类型名
     */
    private final String name;

}
