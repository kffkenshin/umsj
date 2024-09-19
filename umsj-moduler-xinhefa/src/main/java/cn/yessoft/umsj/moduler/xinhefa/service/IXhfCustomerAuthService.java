package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerAuthVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerAuthDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客服权限表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
public interface IXhfCustomerAuthService extends IService<XhfCustomerAuthDO> {

    List<XhfCustomerAuthDO> queryAuthList(CustomerQueryReqVO reqVO);

    void delete(List<Long> ids);

    void create(CustomerAuthVO reqVO);

    void update(CustomerAuthVO reqVO);

    XhfCustomerAuthDO validateExist(Long id);
}
