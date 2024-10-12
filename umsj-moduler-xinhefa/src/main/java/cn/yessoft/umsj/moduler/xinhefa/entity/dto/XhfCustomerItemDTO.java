package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客户物料表
 * </p>
 *
 * @author ethan
 * @since 2024-09-24
 */
@Getter
@Setter
public class XhfCustomerItemDTO {

    /**
     * 客户料号编号
     */
    private String customerItemNo;

    private String customerName;

    /**
     * 客户料号名称
     */
    private String customerItemName;

    /**
     * 客户料号规格
     */
    private String customerItemSpec;

    private String itemNo;

    private String itemName;

    private String itemSpec;
}
