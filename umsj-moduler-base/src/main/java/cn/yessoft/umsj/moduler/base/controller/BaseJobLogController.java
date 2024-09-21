package cn.yessoft.umsj.moduler.base.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobLogPageReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobLogRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseJobLogDO;
import cn.yessoft.umsj.moduler.base.service.IBaseJobLogService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;


@RestController
@RequestMapping("/api/base/job-log")
@Validated
public class BaseJobLogController {

    @Resource
    private IBaseJobLogService baseJobLogService;

    @GetMapping("/get")
    public ApiResult<JobLogRespVO> getJobLog(@RequestParam("id") Long id) {
        BaseJobLogDO jobLog = baseJobLogService.getJobLog(id);
        return success(BeanUtils.toBean(jobLog, JobLogRespVO.class));
    }

    @PostMapping("/paged-query")
    public ApiResult<PageResult<JobLogRespVO>> getJobLogPage(@Valid JobLogPageReqVO pageVO) {
        PageResult<BaseJobLogDO> pageResult = baseJobLogService.getJobLogPage(pageVO);
        return success(BeanUtils.toBean(pageResult, JobLogRespVO.class));
    }

}