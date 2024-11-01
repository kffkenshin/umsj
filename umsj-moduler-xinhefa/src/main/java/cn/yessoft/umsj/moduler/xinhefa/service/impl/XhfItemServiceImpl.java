package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfItemMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 产品类型表 服务实现类
 *
 * @author ethan
 * @since 2024-09-19
 */
@Service("xhfItemService")
public class XhfItemServiceImpl extends ServiceImpl<XhfItemMapper, XhfItemDO>
    implements IXhfItemService {

  @Resource private XhfItemMapper xhfItemMapper;

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
    r.setFirstMachine(reqVO.getFirstMachine());
    r.setSecondMachine(reqVO.getSecondMachine());
    r.setThirdMachine(reqVO.getThirdMachine());
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

  @Override
  public XhfItemDO getItemByNo(String itemNo) {
    return xhfItemMapper.getItemByNo(itemNo);
  }

  @Override
  public void insertOrUpdateBatch(Collection<XhfItemDO> entitys) {
    xhfItemMapper.insertOrUpdateBatch(entitys);
  }

  @Override
  public List<XhfItemDO> getEmptyType2Items() {
    return xhfItemMapper.getEmptyType2Items();
  }

  @Override
  public List<XhfItemDO> getEmptyChannelItems() {
    return xhfItemMapper.getEmptyChannelItems();
  }
}
