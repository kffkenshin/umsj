package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerDO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 客户信息表 服务类
 *
 * @author ethan
 * @since 2024-09-16
 */
public interface IXhfCustomerService extends IService<XhfCustomerDO> {

  PageResult<XhfCustomerDO> pagedQuery(CustomerQueryReqVO reqVO);

  List<XhfCustomerDO> listQuery(CustomerQueryReqVO reqVO);

  void update(CustomerVO reqVO);

  XhfCustomerDO validateExist(Long id);

  XhfCustomerDO getByNo(String 客户编号);
}
