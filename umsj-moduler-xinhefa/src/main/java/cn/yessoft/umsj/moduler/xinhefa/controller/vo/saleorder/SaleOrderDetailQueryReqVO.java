package cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder;

import cn.yessoft.umsj.common.pojo.PageParam;
import java.util.List;
import lombok.Data;

@Data
public class SaleOrderDetailQueryReqVO extends PageParam {
  private String soNumbers;
  private List<String> itemType2;
  private String itemInfo;
  private String customerInfo;
  private List<String> apsStatus;
  private List<String> orderDate;
  private List<String> preDeleverDate;
  private Integer customerSevice;
}
