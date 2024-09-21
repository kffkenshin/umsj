package cn.yessoft.umsj.moduler.base.controller.vo.job;

import cn.yessoft.umsj.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobPageReqVO extends PageParam {

    private String name;

    private Integer status;

    private String handlerName;

}
