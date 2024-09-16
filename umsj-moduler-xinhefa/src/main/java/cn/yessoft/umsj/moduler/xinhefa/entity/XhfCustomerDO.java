package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客户信息表
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
@Getter
@Setter
@TableName("xhf_customer")
public class XhfCustomerDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
}
