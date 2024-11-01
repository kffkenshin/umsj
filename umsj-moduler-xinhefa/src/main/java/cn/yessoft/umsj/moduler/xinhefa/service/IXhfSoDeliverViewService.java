package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.saleorder.SaleOrderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfSoDeliverViewDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * VIEW 服务类
 *
 * @author ethan
 * @since 2024-10-16
 */
public interface IXhfSoDeliverViewService extends IService<XhfSoDeliverViewDO> {

  PageResult<XhfSoDeliverViewDO> pagedQuery(SaleOrderQueryReqVO reqVO);
}
