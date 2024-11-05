package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.mo.MoHeaderQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfManufactureOrderHeaderDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MoHeaderDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 生产订单 服务类
 *
 * @author ethan
 * @since 2024-10-23
 */
public interface IXhfManufactureOrderHeaderService extends IService<XhfManufactureOrderHeaderDO> {

  void createFromSoDeliver(List<Long> soIds);

  PageResult<MoHeaderDTO> pagedQuery(MoHeaderQueryReqVO reqVO);

  String initMo();

  List<XhfManufactureOrderHeaderDO> getHeadersByStatus(String no);
}
