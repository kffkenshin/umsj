package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfCustomerItemDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Collection;

/**
 * 客户物料表 服务类
 *
 * @author ethan
 * @since 2024-09-24
 */
public interface IXhfCustomerItemService extends IService<XhfCustomerItemDO> {

  PageResult<XhfCustomerItemDTO> pagedQuery(InfoPageQueryReqVO reqVO);

  XhfCustomerItemDO getCustomerItem(String cusNo, String itemNo, String customerNo);

  boolean insertBatch(Collection<XhfCustomerItemDO> entities);
}
