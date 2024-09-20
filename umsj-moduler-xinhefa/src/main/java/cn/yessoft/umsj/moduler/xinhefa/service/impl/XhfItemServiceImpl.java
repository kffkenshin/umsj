package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfItemMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

/**
 * <p>
 * 产品类型表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-19
 */
@Service("xhfItemService")
public class XhfItemServiceImpl extends ServiceImpl<XhfItemMapper, XhfItemDO> implements IXhfItemService {

    @Resource
    private XhfItemMapper xhfItemMapper;


    @Override
    public PageResult<XhfItemDO> pagedQuery(ItemQueryVO reqVO) {
        return xhfItemMapper.pagedQuery(reqVO);
    }

    @Override
    public List<XhfItemDO> listQuery(ItemQueryVO reqVO) {
        return xhfItemMapper.listQuery(reqVO);
    }

    @Override
    public void update(ItemVO reqVO) {
        XhfItemDO r = validateExist(reqVO.getId());
        r.setZdDiff(reqVO.getZdDiff());
        r.setRollerLength(reqVO.getRollerLength());
        xhfItemMapper.updateById(r);
    }

    @Override
    public XhfItemDO validateExist(Long id) {
        XhfItemDO r = xhfItemMapper.selectById(id);
        if (r == null) {
            throw exception(O_NOT_EXISTS, "物料");
        }
        return r;
    }
}
