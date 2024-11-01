package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSoDeliverViewDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfSoDeliverViewMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfSoDeliverViewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * VIEW 服务实现类
 *
 * @author ethan
 * @since 2024-10-16
 */
@Service("xhfSoDeliverViewService")
public class XhfSoDeliverViewServiceImpl
    extends ServiceImpl<XhfSoDeliverViewMapper, XhfSoDeliverViewDO>
    implements IXhfSoDeliverViewService {
  @Resource private XhfSoDeliverViewMapper xhfSoDeliverViewMapper;

  @Override
  public PageResult<XhfSoDeliverViewDO> pagedQuery(SaleOrderQueryReqVO reqVO) {
    return xhfSoDeliverViewMapper.pagedQuery(reqVO);
  }
}
