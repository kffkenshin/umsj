package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryListVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessRouteDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductProcessRouteDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 工艺路线表 服务类
 *
 * @author ethan
 * @since 2024-09-21
 */
public interface IXhfProductProcessRouteService extends IService<XhfProductProcessRouteDO> {

  List<XhfProductProcessRouteDTO> listQuery(ItemQueryListVO reqVO);

  void deleteByItemId(Long itemId);

  void insertBatch(List<XhfProductProcessRouteDO> results);

  XhfProductProcessRouteDO getByItemIdAndProcessNo(Long itemId, String processNo);

  List<SimulateDetailDTO> listByitemId(Long itemId);
}
