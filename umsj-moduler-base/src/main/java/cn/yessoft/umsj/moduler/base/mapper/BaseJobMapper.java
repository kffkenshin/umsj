package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.job.JobPageReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseJobDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务 Mapper
 */
@Mapper
public interface BaseJobMapper extends YesBaseMapper<BaseJobDO> {

    default BaseJobDO selectByHandlerName(String handlerName) {
        return selectOne(BaseJobDO::getHandlerName, handlerName);
    }

    default PageResult<BaseJobDO> selectPage(JobPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BaseJobDO>()
                .likeIfPresent(BaseJobDO::getName, reqVO.getName())
                .eqIfPresent(BaseJobDO::getStatus, reqVO.getStatus())
                .likeIfPresent(BaseJobDO::getHandlerName, reqVO.getHandlerName())
        );
    }

}
