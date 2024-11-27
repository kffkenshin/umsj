package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryListVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductBomDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductBomDTO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfProductBomMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductBomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * BOM表 服务实现类
 *
 * @author ethan
 * @since 2024-09-21
 */
@Service("xhfProductBomService")
public class XhfProductBomServiceImpl extends ServiceImpl<XhfProductBomMapper, XhfProductBomDO>
    implements IXhfProductBomService {
  @Resource private XhfProductBomMapper xhfProductBomMapper;

  @Override
  public List<XhfProductBomDTO> listQuery(ItemQueryListVO reqVO) {
    return xhfProductBomMapper.listQuery(reqVO);
  }

  @Override
  public void deleteByItemId(Long itemId) {
    xhfProductBomMapper.delete(XhfProductBomDO::getItemId, itemId);
  }

  @Override
  public void insertBatch(List<XhfProductBomDO> results) {
    xhfProductBomMapper.insertBatch(results);
  }

  @Override
  public List<XhfProductBomDTO> getByItemId(Long itemId) {
    return xhfProductBomMapper.getByItemId(itemId);
  }
}
