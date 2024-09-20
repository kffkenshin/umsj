package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import cn.yessoft.umsj.common.core.EmptyStringListDeserializer;
import cn.yessoft.umsj.common.pojo.PageParam;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class ItemQueryVO extends PageParam {

    @JsonDeserialize(using = EmptyStringListDeserializer.class)
    private List<String> itemType;

    @JsonDeserialize(using = EmptyStringListDeserializer.class)
    private List<String> itemType2;

    private String info;

}
