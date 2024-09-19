package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import lombok.Data;

import java.util.List;

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

    private Long customerId;

    /**
     * 负责账号ID
     */
    private Long accountId;

    /**
     * 负责账号名称
     */
    private String accountName;

    /**
     * 产品类型
     */
    private List<String> itemTypes;

    /**
     * 产品类型
     */
    private String itemTypesStr;

    /**
     * 角色ID
     */
    private Long roleId;

    public String getItemTypesStr() {
        return itemTypes.toString();
    }
}
