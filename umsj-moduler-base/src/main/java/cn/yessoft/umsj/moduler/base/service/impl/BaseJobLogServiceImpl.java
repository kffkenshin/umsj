package cn.yessoft.umsj.moduler.base.service.impl;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobLogPageReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseJobLogDO;
import cn.yessoft.umsj.moduler.base.enums.JobLogStatusEnum;
import cn.yessoft.umsj.moduler.base.mapper.BaseJobLogMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseJobLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * Job 日志 Service 实现类
 */
@Service("baseJobLogService")
@Validated
@Slf4j
public class BaseJobLogServiceImpl implements IBaseJobLogService {

    @Resource
    private BaseJobLogMapper baseJobLogMapper;

    @Override
    public Long createJobLog(Long jobId, LocalDateTime beginTime,
                             String jobHandlerName, String jobHandlerParam, Integer executeIndex) {
        BaseJobLogDO log = BaseJobLogDO.builder().jobId(jobId).handlerName(jobHandlerName)
                .handlerParam(jobHandlerParam).executeIndex(executeIndex)
                .beginTime(beginTime).status(JobLogStatusEnum.RUNNING.getStatus()).build();
        baseJobLogMapper.insert(log);
        return log.getId();
    }

    @Override
    @Async
    public void updateJobLogResultAsync(Long logId, LocalDateTime endTime, Integer duration, boolean success, String result) {
        try {
            BaseJobLogDO updateObj = BaseJobLogDO.builder().id(logId).endTime(endTime).duration(duration)
                    .status(success ? JobLogStatusEnum.SUCCESS.getStatus() : JobLogStatusEnum.FAILURE.getStatus())
                    .result(result).build();
            baseJobLogMapper.updateById(updateObj);
        } catch (Exception ex) {
            log.error("[updateJobLogResultAsync][logId({}) endTime({}) duration({}) success({}) result({})]",
                    logId, endTime, duration, success, result);
        }
    }


    @Override
    public BaseJobLogDO getJobLog(Long id) {
        return baseJobLogMapper.selectById(id);
    }

    @Override
    public PageResult<BaseJobLogDO> getJobLogPage(JobLogPageReqVO pageReqVO) {
        return baseJobLogMapper.selectPage(pageReqVO);
    }

}
