package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobPageReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobSaveReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseJobDO;
import jakarta.validation.Valid;
import org.quartz.SchedulerException;

/**
 * 定时任务 Service 接口
 */
public interface IBaseJobService {

    /**
     * 创建定时任务
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createJob(@Valid JobSaveReqVO createReqVO) throws SchedulerException;

    /**
     * 更新定时任务
     *
     * @param updateReqVO 更新信息
     */
    void updateJob(@Valid JobSaveReqVO updateReqVO) throws SchedulerException;

    /**
     * 更新定时任务的状态
     *
     * @param id     任务编号
     * @param status 状态
     */
    void updateJobStatus(Long id, Integer status) throws SchedulerException;

    /**
     * 触发定时任务
     *
     * @param id 任务编号
     */
    void triggerJob(Long id) throws SchedulerException;

    /**
     * 同步定时任务
     * <p>
     * 目的：自己存储的 Job 信息，强制同步到 Quartz 中
     */
    void syncJob() throws SchedulerException;

    /**
     * 删除定时任务
     *
     * @param id 编号
     */
    void deleteJob(Long id) throws SchedulerException;

    /**
     * 获得定时任务
     *
     * @param id 编号
     * @return 定时任务
     */
    BaseJobDO getJob(Long id);

    /**
     * 获得定时任务分页
     *
     * @param pageReqVO 分页查询
     * @return 定时任务分页
     */
    PageResult<BaseJobDO> getJobPage(JobPageReqVO pageReqVO);

}
