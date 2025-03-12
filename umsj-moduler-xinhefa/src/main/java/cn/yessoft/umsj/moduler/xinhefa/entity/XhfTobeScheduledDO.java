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
 * 待排产机台
 * </p>
 *
 * @author ethan
 * @since 2024-11-07
 */
@Getter
@Setter
@TableName("xhf_tobe_scheduled")
public class XhfTobeScheduledDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工作站
     */
    private Integer workStation;

    /**
     * 机台号
     */
    private String machineNo;

    /**
     * 排产模式
     */
    private String mode;
}
