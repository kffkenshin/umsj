package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfCustomerMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 客户信息表 服务实现类
 *
 * @author ethan
 * @since 2024-09-16
 */
@Service("xhfCustomerService")
public class XhfCustomerServiceImpl extends ServiceImpl<XhfCustomerMapper, XhfCustomerDO>
    implements IXhfCustomerService {

  @Resource private XhfCustomerMapper xhfCustomerMapper;

  @Override
  public PageResult<XhfCustomerDO> pagedQuery(CustomerQueryReqVO reqVO) {
    return xhfCustomerMapper.pagedQuery(reqVO);
  }

  @Override
  public List<XhfCustomerDO> listQuery(CustomerQueryReqVO reqVO) {
    return xhfCustomerMapper.listQuery(reqVO);
  }

  @Override
  public void update(CustomerVO reqVO) {
    XhfCustomerDO r = validateExist(reqVO.getId());
    r.setDeliveryDays(reqVO.getDeliveryDays());
    r.setMatchStrategy(reqVO.getMatchStrategy());
    r.setProductRequiredDays(reqVO.getProductRequiredDays());
    r.setWarehousingLeadDays(reqVO.getWarehousingLeadDays());
    r.setLogisticsDays(reqVO.getLogisticsDays());
    xhfCustomerMapper.updateById(r);
  }

  @Override
  public XhfCustomerDO validateExist(Long id) {
    XhfCustomerDO r = xhfCustomerMapper.selectById(id);
    if (r == null) {
      throw exception(O_NOT_EXISTS, "客户");
    }
    return r;
  }

  @Override
  public XhfCustomerDO getByNo(String cusNo) {
    return xhfCustomerMapper.getByNo(cusNo);
  }
}
