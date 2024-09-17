package cn.yessoft.umsj.moduler.xinhefa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * APS系统固定角色
 */
@Getter
@AllArgsConstructor
public enum XHFRoleEnum {

    CUSTOMER_SERVICE(2L), // 客服
    ;

    /**
     * 角色no
     */
    private final Long roleId;

}
