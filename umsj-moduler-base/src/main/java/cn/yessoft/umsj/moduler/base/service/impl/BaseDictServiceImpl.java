package cn.yessoft.umsj.moduler.base.service.impl;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictQueryPageReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDO;
import cn.yessoft.umsj.moduler.base.mapper.BaseDictMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
@Service("baseDictService")
public class BaseDictServiceImpl extends ServiceImpl<BaseDictMapper, BaseDictDO> implements IBaseDictService {

    @Resource
    private BaseDictMapper baseDictMapper;

    @Override
    public PageResult<BaseDictDO> pagedQuery(DictQueryPageReqVO reqVO) {
        return baseDictMapper.pagedQuery(reqVO);
    }
}
