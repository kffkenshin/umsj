package cn.yessoft.umsj.moduler.xinhefa.entity;

import cn.yessoft.umsj.mybatis.core.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * BOM表
 * </p>
 *
 * @author ethan
 * @since 2024-09-21
 */
@Getter
@Setter
@TableName("xhf_product_bom")
public class XhfProductBomDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 物料ID
     */
    private Long itemId;

    /**
     * 工序ID
     */
    private Long processRouteId;

    /**
     * 材料ID
     */
    private Long materialId;

    /**
     * 主件底数
     */
    private BigDecimal baseNumber;

    private Integer seq;
}
