package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

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
public class XhfProductBomDTO {

    private Integer seq;

    private String materialNo;

    private String materialName;

    private BigDecimal baseNumber;

    private String processNo;

    private String processName;
}
