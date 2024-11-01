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
 * 选机标准
 * </p>
 *
 * @author ethan
 * @since 2024-10-19
 */
@Getter
@Setter
@TableName("xhf_static_machine_determinant")
public class XhfStaticMachineDeterminantDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 料号ID
     */
    private Long itemId;

    /**
     * 材料名称
     */
    private String itemName;

    /**
     * 类型
     */
    private String type;

    /**
     * 首选机台
     */
    private Long firstMachineId;

    /**
     * 二选机台
     */
    private Long secondMachineId;

    /**
     * 三选机台
     */
    private Long thirdMachineId;

    /**
     * 备注
     */
    private String remark;
}
