package cn.yessoft.umsj.moduler.base.controller.vo.job;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobSaveReqVO {

    private Long id;

    @NotEmpty(message = "任务名称不能为空")
    private String name;

    @NotEmpty(message = "处理器的名字不能为空")
    private String handlerName;

    private String handlerParam;

    @NotEmpty(message = "CRON 表达式不能为空")
    private String cronExpression;

    @NotNull(message = "重试次数不能为空")
    private Integer retryCount;

    @NotNull(message = "重试间隔不能为空")
    private Integer retryInterval;

    private Integer monitorTimeout;

}
