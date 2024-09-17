package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemTypeQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemTypeRespVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemTypeDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfItemTypeMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemTypeService;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_DUPLICATE;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

/**
 * <p>
 * 产品类型表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
@Service("xhfItemTypeService")
public class XhfItemTypeServiceImpl extends ServiceImpl<XhfItemTypeMapper, XhfItemTypeDO> implements IXhfItemTypeService {
    @Resource
    private XhfItemTypeMapper xhfItemTypeMapper;

    @Override
    public PageResult<XhfItemTypeDO> pagedQuery(ItemTypeQueryReqVO reqVO) {
        return xhfItemTypeMapper.selectPage(reqVO, new LambdaQueryWrapperX<XhfItemTypeDO>().
                eqIfPresent(XhfItemTypeDO::getTypeClass, reqVO.getTypeClass()));
    }

    @Override
    public List<XhfItemTypeDO> listQuery(ItemTypeQueryReqVO reqVO) {
        return xhfItemTypeMapper.selectList(new Page<>(0, 100), new LambdaQueryWrapperX<XhfItemTypeDO>().
                eq(XhfItemTypeDO::getTypeClass, reqVO.getTypeClass()));
    }

    @Override
    public void create(ItemTypeRespVO reqVO) {
        // 验证唯一性
        validateUnique(reqVO.getTypeClass(), reqVO.getName());
        XhfItemTypeDO r = BeanUtil.toBean(reqVO, XhfItemTypeDO.class);
        xhfItemTypeMapper.insert(r);
    }

    private void validateUnique(String typeClass, String name) {
        XhfItemTypeDO r = getByTypeAndName(typeClass, name);
        if (r != null) {
            throw exception(O_DUPLICATE, "类型");
        }
    }

    @Override
    public XhfItemTypeDO getByTypeAndName(String typeClass, String name) {
        return xhfItemTypeMapper.selectOne(XhfItemTypeDO::getTypeClass, typeClass, XhfItemTypeDO::getName, name);
    }

    @Override
    public void update(ItemTypeRespVO reqVO) {
        XhfItemTypeDO r = validateExist(reqVO.getId());
        r = BeanUtil.toBean(reqVO, XhfItemTypeDO.class);
        xhfItemTypeMapper.updateById(r);
    }

    @Override
    public XhfItemTypeDO validateExist(Long id) {
        XhfItemTypeDO r = xhfItemTypeMapper.selectById(id);
        if (r == null) {
            throw exception(O_NOT_EXISTS, "类型");
        }
        return r;
    }

    @Override
    public void delete(List<Long> ids) {
        xhfItemTypeMapper.deleteBatchIds(ids);
    }
}
