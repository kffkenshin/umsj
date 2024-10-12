package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/** 放量、废品率试算 */
@Getter
@Setter
public class RejectRateSimulateVO {
  @NotBlank(message = "料号不能为空")
  private String itemNo;

  @NotNull(message = "数量不能为空")
  private BigDecimal qty;
}
