package cn.yessoft.umsj.moduler.base.controller.vo.menu;

import cn.yessoft.umsj.common.pojo.PageParam;
import lombok.Data;

@Data
public class MenuQueryReqVO extends PageParam {

    private String name;

    private Integer type;

}
