package cn.yessoft.umsj.moduler.base.controller.vo.job;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobLogRespVO {

    private Long id;

    private Long jobId;

    private String handlerName;

    private String handlerParam;

    private Integer executeIndex;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer duration;

    private Integer status;

    private String result;

    private LocalDateTime createTime;

}
