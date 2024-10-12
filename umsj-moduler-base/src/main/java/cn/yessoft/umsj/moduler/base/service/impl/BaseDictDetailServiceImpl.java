package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictDetailVO;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictQueryReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDetailDO;
import cn.yessoft.umsj.moduler.base.mapper.BaseDictDetailMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseDictDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_DUPLICATE;

/**
 * <p>
 * 数据字典明细表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
@Service("baseDictDetailService")
public class BaseDictDetailServiceImpl extends ServiceImpl<BaseDictDetailMapper, BaseDictDetailDO> implements IBaseDictDetailService {


    @Resource
    private BaseDictDetailMapper baseDictDetailMapper;


    @Override
    public List<BaseDictDetailDO> listQuery(DictQueryReqVO reqVO) {
        return baseDictDetailMapper.listQuery(reqVO);
    }

    @Override
    public void deleteDetail(List<Long> ids) {
        baseDictDetailMapper.deleteBatchIds(ids);
    }

    @Override
    public void create(DictDetailVO reqVO) {
        // 验证唯一性
        validateUnique(reqVO.getDictId(), reqVO.getDetailKey());
        BaseDictDetailDO r = BeanUtil.toBean(reqVO, BaseDictDetailDO.class);
        baseDictDetailMapper.insert(r);
    }

    private void validateUnique(Long dictId, String dKey) {
        BaseDictDetailDO entity = baseDictDetailMapper.selectOne(BaseDictDetailDO::getDictId, dictId,
                BaseDictDetailDO::getDetailKey, dKey);
        if (entity != null) {
            throw exception(O_DUPLICATE, "字典值");
        }
    }

    @Override
    public BaseDictDetailDO getByDictIdAndDetailKey(Integer dictId, String detailKey) {
        return baseDictDetailMapper.getByDictIdAndDetailKey(dictId, detailKey);
    }
}
