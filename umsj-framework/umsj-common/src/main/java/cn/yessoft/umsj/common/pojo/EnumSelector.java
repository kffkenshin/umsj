package cn.yessoft.umsj.common.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnumSelector implements Serializable {

  private String value;
  private String text;
}
