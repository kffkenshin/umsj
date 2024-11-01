package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 机台表 服务类
 *
 * @author ethan
 * @since 2024-10-17
 */
public interface IXhfMachineService extends IService<XhfMachineDO> {

  PageResult<XhfMachineDO> pagedQuery(InfoPageQueryReqVO reqVO);

  void delete(List<Long> ids);
}
