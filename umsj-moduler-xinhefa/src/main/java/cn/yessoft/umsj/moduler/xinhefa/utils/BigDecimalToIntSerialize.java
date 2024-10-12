package cn.yessoft.umsj.moduler.xinhefa.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalToIntSerialize extends JsonSerializer<BigDecimal> {
  @Override
  public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializerProvider)
      throws IOException {
    if (value != null && !"".equals(value)) {
      gen.writeString(value.setScale(0, BigDecimal.ROUND_HALF_DOWN) + "");
    } else {
      gen.writeString(value + "");
    }
  }
}
