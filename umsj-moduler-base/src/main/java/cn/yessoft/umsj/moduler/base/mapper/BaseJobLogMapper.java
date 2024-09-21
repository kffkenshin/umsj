package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobLogPageReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseJobLogDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务日志 Mapper
 */
@Mapper
public interface BaseJobLogMapper extends YesBaseMapper<BaseJobLogDO> {

    default PageResult<BaseJobLogDO> selectPage(JobLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BaseJobLogDO>()
                .eqIfPresent(BaseJobLogDO::getJobId, reqVO.getJobId())
                .likeIfPresent(BaseJobLogDO::getHandlerName, reqVO.getHandlerName())
                .geIfPresent(BaseJobLogDO::getBeginTime, reqVO.getBeginTime())
                .leIfPresent(BaseJobLogDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(BaseJobLogDO::getStatus, reqVO.getStatus())
                .orderByDesc(BaseJobLogDO::getId) // ID 倒序
        );
    }

}
