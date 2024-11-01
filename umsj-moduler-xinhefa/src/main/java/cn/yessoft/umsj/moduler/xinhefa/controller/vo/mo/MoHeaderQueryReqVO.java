package cn.yessoft.umsj.moduler.xinhefa.controller.vo.mo;

import cn.yessoft.umsj.common.pojo.PageParam;
import java.util.List;
import lombok.Data;

@Data
public class MoHeaderQueryReqVO extends PageParam {
  private String docNumbers;
  private List<String> itemType2;
  private String itemInfo;
  private String customerInfo;
  private List<String> apsStatus;
  private List<String> orderDate;
  private Integer manufactureMan;
  private String quickAccess;
}
