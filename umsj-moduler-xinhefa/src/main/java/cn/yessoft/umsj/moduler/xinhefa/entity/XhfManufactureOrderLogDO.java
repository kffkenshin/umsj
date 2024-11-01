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
 * 生产订单日志
 * </p>
 *
 * @author ethan
 * @since 2024-10-23
 */
@Getter
@Setter
@TableName("xhf_manufacture_order_log")
public class XhfManufactureOrderLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 生产工单头ID
     */
    private Long headerId;

    /**
     * 操作动作
     */
    private String action;

    /**
     * 日志
     */
    private String log;
}
