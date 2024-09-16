package cn.yessoft.umsj.moduler.base.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色类型
 */
@Getter
@AllArgsConstructor
public enum RoleTpyeEnum {
    ACCOUNT(1, "账号"),
    ROLE(2, "角色");
    /**
     * 类型
     */
    private final Integer value;
    /**
     * 类型名
     */
    private final String name;

    public static RoleTpyeEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), RoleTpyeEnum.values());
    }
}
