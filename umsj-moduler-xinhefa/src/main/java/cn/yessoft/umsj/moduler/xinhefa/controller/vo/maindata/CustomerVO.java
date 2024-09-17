package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import lombok.Data;

/**
 * <p>
 * 客户信息表
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
@Data
public class CustomerVO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 客户编号
     */
    private String no;

    /**
     * 客户名称(中文)
     */
    private String nameCn;

    /**
     * 集团客户
     */
    private String cusGroup;

    /**
     * 客户名称(英文)
     */
    private String nameEn;

    /**
     * 客户级别
     */
    private String level;

    /**
     * 客户简称
     */
    private String nameShort;

    /**
     * 地址
     */
    private String address;

    /**
     * 约定交期天数
     */
    private Integer deliveryDays;

    /**
     * 物流天数
     */
    private Integer logisticsDays;

    /**
     * 入库提前期
     */
    private Integer warehousingLeadDays;

    /**
     * 生产需求周期
     */
    private Integer productRequiredDays;

    /**
     * 导未交客户单号填写验证
     */
    private Boolean needSaleNo;

    /**
     * 匹配策略
     */
    private Integer matchStrategy;

    private String matchStrategyStr;
}
