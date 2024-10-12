package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobPageReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobSaveReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseJobDO;
import cn.yessoft.umsj.moduler.base.enums.JobStatusEnum;
import cn.yessoft.umsj.moduler.base.mapper.BaseJobMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseJobService;
import cn.yessoft.umsj.quartz.core.handler.JobHandler;
import cn.yessoft.umsj.quartz.core.scheduler.SchedulerManager;
import cn.yessoft.umsj.quartz.core.util.CronUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.common.utils.CollectionUtils.containsAny;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.*;


/**
 * 定时任务 Service 实现类
 *
 * @author 芋道源码
 */
@Service("baseJobService")
@Validated
@Slf4j
public class BaseJobServiceImpl implements IBaseJobService {

    @Resource
    private BaseJobMapper baseJobMapper;

    @Resource
    private SchedulerManager schedulerManager;

    @Value("${yesee.base.job-scheduler}")
    private String JOB_SCHEDULER;

    @PostConstruct
    void init() throws SchedulerException {
        if ("ON".equals(JOB_SCHEDULER)) {
            syncJob();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createJob(JobSaveReqVO createReqVO) throws SchedulerException {
        validateCronExpression(createReqVO.getCronExpression());
        // 1.1 校验唯一性
        if (baseJobMapper.selectByHandlerName(createReqVO.getHandlerName()) != null) {
            throw exception(JOB_HANDLER_EXISTS);
        }
        // 1.2 校验 JobHandler 是否存在
        validateJobHandlerExists(createReqVO.getHandlerName());

        // 2. 插入 JobDO
        BaseJobDO job = BeanUtils.toBean(createReqVO, BaseJobDO.class);
        job.setStatus(JobStatusEnum.INIT.getStatus());
        fillJobMonitorTimeoutEmpty(job);
        baseJobMapper.insert(job);

        // 3.1 添加 Job 到 Quartz 中
        schedulerManager.addJob(job.getId(), job.getHandlerName(), job.getHandlerParam(), job.getCronExpression(),
                createReqVO.getRetryCount(), createReqVO.getRetryInterval());
        // 3.2 更新 JobDO
        BaseJobDO updateObj = BaseJobDO.builder().id(job.getId()).status(JobStatusEnum.NORMAL.getStatus()).build();
        baseJobMapper.updateById(updateObj);
        return job.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(JobSaveReqVO updateReqVO) throws SchedulerException {
        validateCronExpression(updateReqVO.getCronExpression());
        // 1.1 校验存在
        BaseJobDO job = validateJobExists(updateReqVO.getId());
        // 1.2 只有开启状态，才可以修改.原因是，如果出暂停状态，修改 Quartz Job 时，会导致任务又开始执行
        if (!job.getStatus().equals(JobStatusEnum.NORMAL.getStatus())) {
            throw exception(JOB_UPDATE_ONLY_NORMAL_STATUS);
        }
        // 1.3 校验 JobHandler 是否存在
        validateJobHandlerExists(updateReqVO.getHandlerName());

        // 2. 更新 JobDO
        BaseJobDO updateObj = BeanUtils.toBean(updateReqVO, BaseJobDO.class);
        fillJobMonitorTimeoutEmpty(updateObj);
        baseJobMapper.updateById(updateObj);

        // 3. 更新 Job 到 Quartz 中
        schedulerManager.updateJob(job.getHandlerName(), updateReqVO.getHandlerParam(), updateReqVO.getCronExpression(),
                updateReqVO.getRetryCount(), updateReqVO.getRetryInterval());
    }

    private void validateJobHandlerExists(String handlerName) {
        Object handler = SpringUtil.getBean(handlerName);
        if (handler == null) {
            throw exception(JOB_HANDLER_BEAN_NOT_EXISTS);
        }
        if (!(handler instanceof JobHandler)) {
            throw exception(JOB_HANDLER_BEAN_TYPE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobStatus(Long id, Integer status) throws SchedulerException {
        // 校验 status
        if (!containsAny(status, JobStatusEnum.NORMAL.getStatus(), JobStatusEnum.STOP.getStatus())) {
            throw exception(JOB_CHANGE_STATUS_INVALID);
        }
        // 校验存在
        BaseJobDO job = validateJobExists(id);
        // 校验是否已经为当前状态
        if (job.getStatus().equals(status)) {
            throw exception(JOB_CHANGE_STATUS_EQUALS);
        }
        // 更新 Job 状态
        BaseJobDO updateObj = BaseJobDO.builder().id(id).status(status).build();
        baseJobMapper.updateById(updateObj);

        // 更新状态 Job 到 Quartz 中
        if (JobStatusEnum.NORMAL.getStatus().equals(status)) { // 开启
            schedulerManager.resumeJob(job.getHandlerName());
        } else { // 暂停
            schedulerManager.pauseJob(job.getHandlerName());
        }
    }

    @Override
    public void triggerJob(Long id) throws SchedulerException {
        // 校验存在
        BaseJobDO job = validateJobExists(id);

        // 触发 Quartz 中的 Job
        schedulerManager.triggerJob(job.getId(), job.getHandlerName(), job.getHandlerParam());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncJob() throws SchedulerException {
        // 1. 查询 Job 配置
        List<BaseJobDO> jobList = baseJobMapper.selectList();

        // 2. 遍历处理
        for (BaseJobDO job : jobList) {
            // 2.1 先删除，再创建
            schedulerManager.deleteJob(job.getHandlerName());
            schedulerManager.addJob(job.getId(), job.getHandlerName(), job.getHandlerParam(), job.getCronExpression(),
                    job.getRetryCount(), job.getRetryInterval());
            // 2.2 如果 status 为暂停，则需要暂停
            if (Objects.equals(job.getStatus(), JobStatusEnum.STOP.getStatus())) {
                schedulerManager.pauseJob(job.getHandlerName());
            }
            log.info("[syncJob][id({}) handlerName({}) 同步完成]", job.getId(), job.getHandlerName());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(Long id) throws SchedulerException {
        // 校验存在
        BaseJobDO job = validateJobExists(id);
        // 更新
        baseJobMapper.deleteById(id);

        // 删除 Job 到 Quartz 中
        schedulerManager.deleteJob(job.getHandlerName());
    }

    private BaseJobDO validateJobExists(Long id) {
        BaseJobDO job = baseJobMapper.selectById(id);
        if (job == null) {
            throw exception(JOB_NOT_EXISTS);
        }
        return job;
    }

    private void validateCronExpression(String cronExpression) {
        if (!CronUtils.isValid(cronExpression)) {
            throw exception(JOB_CRON_EXPRESSION_VALID);
        }
    }

    @Override
    public BaseJobDO getJob(Long id) {
        return baseJobMapper.selectById(id);
    }

    @Override
    public PageResult<BaseJobDO> getJobPage(JobPageReqVO pageReqVO) {
        return baseJobMapper.selectPage(pageReqVO);
    }

    private static void fillJobMonitorTimeoutEmpty(BaseJobDO job) {
        if (job.getMonitorTimeout() == null) {
            job.setMonitorTimeout(0);
        }
    }

}
