package cn.yessoft.umsj.moduler.base.controller.vo.job;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobRespVO {

    private Long id;

    private String name;

    private Integer status;

    private String handlerName;

    private String handlerParam;

    private String cronExpression;

    private Integer retryCount;

    private Integer retryInterval;

    private Integer monitorTimeout;

    private LocalDateTime createTime;

}
