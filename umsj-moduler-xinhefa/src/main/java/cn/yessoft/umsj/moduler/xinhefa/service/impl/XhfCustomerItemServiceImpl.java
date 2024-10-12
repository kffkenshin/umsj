package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfCustomerItemDTO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfCustomerItemMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.Collection;
import org.springframework.stereotype.Service;

/**
 * 客户物料表 服务实现类
 *
 * @author ethan
 * @since 2024-09-24
 */
@Service("xhfCustomerItemService")
public class XhfCustomerItemServiceImpl
    extends ServiceImpl<XhfCustomerItemMapper, XhfCustomerItemDO>
    implements IXhfCustomerItemService {

  @Resource private XhfCustomerItemMapper xhfCustomerItemMapper;

  @Override
  public PageResult<XhfCustomerItemDTO> pagedQuery(InfoPageQueryReqVO reqVO) {
    return xhfCustomerItemMapper.pagedQuery(reqVO);
  }

  @Override
  public XhfCustomerItemDO getCustomerItem(
      String customerItemNo, String itemNo, String customerNo) {
    return xhfCustomerItemMapper.getCustomerItem(customerItemNo, itemNo, customerNo);
  }

  @Override
  public boolean insertBatch(Collection<XhfCustomerItemDO> entities) {
    return xhfCustomerItemMapper.insertBatch(entities);
  }
}
