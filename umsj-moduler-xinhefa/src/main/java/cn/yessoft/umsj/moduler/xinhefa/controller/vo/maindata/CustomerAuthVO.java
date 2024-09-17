package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import lombok.Data;

/**
 * <p>
 * 客服权限表
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
@Data
public class CustomerAuthVO {

    private Long id;

    /**
     * 负责账号ID
     */
    private Long accountId;

    /**
     * 产品类型
     */
    private String itemTypeIds;

    /**
     * 角色ID
     */
    private Long roleId;
}
