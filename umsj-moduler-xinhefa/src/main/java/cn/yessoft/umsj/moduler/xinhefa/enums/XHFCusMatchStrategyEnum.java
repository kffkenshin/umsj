package cn.yessoft.umsj.moduler.xinhefa.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 匹配策略
 */
@Getter
@AllArgsConstructor
public enum XHFCusMatchStrategyEnum {

    CONTRACT_DATE(1, "约定交期"), // 约定交期
    CREATED_DATE(2, "下单日期"), // 下单日期
    ORDER_NO(3, "订单单号"), // 订单单号
    ;

    /**
     * 角色no
     */
    private final Integer strategy;

    private final String name;

    public static XHFCusMatchStrategyEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(e -> e.getStrategy().equals(value), XHFCusMatchStrategyEnum.values());
    }
}
