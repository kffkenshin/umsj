package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictQueryReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDetailDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * <p>
 * 数据字典明细表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
public interface BaseDictDetailMapper extends YesBaseMapper<BaseDictDetailDO> {

    default List<BaseDictDetailDO> listQuery(DictQueryReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BaseDictDetailDO>()
                .eq(BaseDictDetailDO::getDictId, reqVO.getDictId()).orderByAsc(BaseDictDetailDO::getSeq));
    }

    default BaseDictDetailDO getByDictIdAndDetailKey(Integer dictId, String detailKey) {
        return selectOne(new LambdaQueryWrapper<BaseDictDetailDO>().eq(BaseDictDetailDO::getDictId, dictId).eq(BaseDictDetailDO::getDetailKey, detailKey));
    }

    ;
}
