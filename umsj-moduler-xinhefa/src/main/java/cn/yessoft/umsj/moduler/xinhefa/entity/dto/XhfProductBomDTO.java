package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * BOM表
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

  private String materialSpec;

  private BigDecimal materialLength;

  private BigDecimal materialWidth;

  private BigDecimal materialTickness;

  private BigDecimal baseNumber;

  private String processNo;

  private String processName;
}
