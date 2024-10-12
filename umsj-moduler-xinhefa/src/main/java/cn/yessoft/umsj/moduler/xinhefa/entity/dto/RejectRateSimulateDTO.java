package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import cn.yessoft.umsj.common.pojo.LableValue;
import cn.yessoft.umsj.common.utils.BaseUtils;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.compress.utils.Lists;

/** 试算结果 */
@Getter
@Setter
public class RejectRateSimulateDTO {

  private List<LableValue> intro;

  private List<SimulateDetailDTO> detail;

  public void addValue(String label, String value) {
    if (intro == null) {
      intro = Lists.newArrayList();
    }
    LableValue lv = new LableValue();
    lv.setValue(value);
    lv.setLabel(label);
    intro.add(lv);
  }

  public SimulateDetailDTO createDetail() {
    if (BaseUtils.isEmpty(detail)) {
      detail = Lists.newArrayList();
    }
    SimulateDetailDTO res = new SimulateDetailDTO();
    detail.add(res);
    return res;
  }
}
