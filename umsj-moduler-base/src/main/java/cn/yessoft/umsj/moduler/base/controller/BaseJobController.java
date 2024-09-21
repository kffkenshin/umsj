package cn.yessoft.umsj.moduler.base.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobPageReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobRespVO;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobSaveReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseJobDO;
import cn.yessoft.umsj.moduler.base.service.IBaseJobService;
import cn.yessoft.umsj.quartz.core.util.CronUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;


@RestController
@RequestMapping("/api/base/job")
@Validated
public class BaseJobController {

    @Resource
    private IBaseJobService baseJobService;

    @PostMapping("/create")
    public ApiResult<Long> createJob(@Valid @RequestBody JobSaveReqVO createReqVO)
            throws SchedulerException {
        return success(baseJobService.createJob(createReqVO));
    }

    @PostMapping("/update")
    public ApiResult<Boolean> updateJob(@Valid @RequestBody JobSaveReqVO updateReqVO)
            throws SchedulerException {
        baseJobService.updateJob(updateReqVO);
        return success(true);
    }

    @PostMapping("/update-status")
    public ApiResult<Boolean> updateJobStatus(@RequestParam(value = "id") Long id, @RequestParam("status") Integer status)
            throws SchedulerException {
        baseJobService.updateJobStatus(id, status);
        return success(true);
    }

    @PostMapping("/delete")
    public ApiResult<Boolean> deleteJob(@RequestParam("id") Long id)
            throws SchedulerException {
        baseJobService.deleteJob(id);
        return success(true);
    }

    @PostMapping("/trigger")
    public ApiResult<Boolean> triggerJob(@RequestParam("id") Long id) throws SchedulerException {
        baseJobService.triggerJob(id);
        return success(true);
    }

    @PostMapping("/sync")
    public ApiResult<Boolean> syncJob() throws SchedulerException {
        baseJobService.syncJob();
        return success(true);
    }

    @PostMapping("/get")
    public ApiResult<JobRespVO> getJob(@RequestParam("id") Long id) {
        BaseJobDO job = baseJobService.getJob(id);
        return success(BeanUtils.toBean(job, JobRespVO.class));
    }

    @PostMapping("/paged-query")
    public ApiResult<PageResult<JobRespVO>> getJobPage(@Valid JobPageReqVO pageVO) {
        PageResult<BaseJobDO> pageResult = baseJobService.getJobPage(pageVO);
        return success(BeanUtils.toBean(pageResult, JobRespVO.class));
    }


    @GetMapping("/get_next_times")
    public ApiResult<List<LocalDateTime>> getJobNextTimes(
            @RequestParam("id") Long id,
            @RequestParam(value = "count", required = false, defaultValue = "5") Integer count) {
        BaseJobDO job = baseJobService.getJob(id);
        if (job == null) {
            return success(Collections.emptyList());
        }
        return success(CronUtils.getNextTimes(job.getCronExpression(), count));
    }

}
