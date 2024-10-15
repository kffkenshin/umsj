package cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class SaleOrderDeliverCreateVO {
  private int id;
  private BigDecimal netInventory;
  private String customerOrderNo;
  private List<SaleOrderDeliverCreateBatchInfoVO> deleverDetail;
}
