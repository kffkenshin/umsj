package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryListVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessRouteDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductProcessRouteDTO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfProductProcessRouteMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductProcessRouteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 工艺路线表 服务实现类
 *
 * @author ethan
 * @since 2024-09-21
 */
@Service("xhfProductProcessRouteService")
public class XhfProductProcessRouteServiceImpl
    extends ServiceImpl<XhfProductProcessRouteMapper, XhfProductProcessRouteDO>
    implements IXhfProductProcessRouteService {

  @Resource private XhfProductProcessRouteMapper xhfProductProcessRouteMapper;

  @Override
  public List<XhfProductProcessRouteDTO> listQuery(ItemQueryListVO reqVO) {
    return xhfProductProcessRouteMapper.listQuery(reqVO);
  }

  @Override
  public void deleteByItemId(Long itemId) {
    xhfProductProcessRouteMapper.delete(XhfProductProcessRouteDO::getItemId, itemId);
  }

  @Override
  public void insertBatch(List<XhfProductProcessRouteDO> results) {
    xhfProductProcessRouteMapper.insertBatch(results);
  }

  @Override
  public XhfProductProcessRouteDO getByItemIdAndProcessNo(Long itemId, String processNo) {
    return xhfProductProcessRouteMapper.getByItemIdAndProcessNo(itemId, processNo);
  }

  @Override
  public List<SimulateDetailDTO> listByitemId(Long itemId) {
    return xhfProductProcessRouteMapper.listByitemId(itemId);
  }
}
