package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictQueryPageReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;

/**
 * <p>
 * 数据字典表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
public interface BaseDictMapper extends YesBaseMapper<BaseDictDO> {

    default PageResult<BaseDictDO> pagedQuery(DictQueryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BaseDictDO>()
                .likeIfPresent(BaseDictDO::getName, reqVO.getInfo())
                .or().likeIfPresent(BaseDictDO::getDescription, reqVO.getInfo())
        );
    }

    ;
}
