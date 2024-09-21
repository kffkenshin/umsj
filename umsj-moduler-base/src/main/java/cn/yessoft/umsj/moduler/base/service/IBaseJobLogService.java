package cn.yessoft.umsj.moduler.base.service;


import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobLogPageReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseJobLogDO;
import cn.yessoft.umsj.quartz.core.service.JobLogFrameworkService;

/**
 * Job 日志 Service 接口
 *
 * @author 芋道源码
 */
public interface IBaseJobLogService extends JobLogFrameworkService {

    /**
     * 获得定时任务
     *
     * @param id 编号
     * @return 定时任务
     */
    BaseJobLogDO getJobLog(Long id);

    /**
     * 获得定时任务分页
     *
     * @param pageReqVO 分页查询
     * @return 定时任务分页
     */
    PageResult<BaseJobLogDO> getJobLogPage(JobLogPageReqVO pageReqVO);


}
