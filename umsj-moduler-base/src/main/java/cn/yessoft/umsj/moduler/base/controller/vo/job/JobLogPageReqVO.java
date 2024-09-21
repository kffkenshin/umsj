package cn.yessoft.umsj.moduler.base.controller.vo.job;

import cn.yessoft.umsj.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobLogPageReqVO extends PageParam {

    private Long jobId;

    private String handlerName;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer status;

}
