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
 * 机台表
 * </p>
 *
 * @author ethan
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("xhf_machine")
public class XhfMachineDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属工作站
     */
    private Integer workStation;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 编号
     */
    private String machineNo;

    /**
     * 名称
     */
    private String machineName;
}
