package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.utils.CacheUtils.buildAsyncReloadingCache;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachinePropertyDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.ProductMachinesDTO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfMachinePropertyMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachinePropertyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 机台产能参数 服务实现类
 *
 * @author ethan
 * @since 2024-10-17
 */
@Service("xhfMachinePropertyService")
public class XhfMachinePropertyServiceImpl
    extends ServiceImpl<XhfMachinePropertyMapper, XhfMachinePropertyDO>
    implements IXhfMachinePropertyService {
  @Resource private XhfMachinePropertyMapper xhfMachinePropertyMapper;

  @Getter
  private final LoadingCache<Long, Map<Integer, ProductMachinesDTO>> clientCache =
      buildAsyncReloadingCache(
          Duration.ofSeconds(100L),
          new CacheLoader<Long, Map<Integer, ProductMachinesDTO>>() {
            @Override
            public Map<Integer, ProductMachinesDTO> load(Long id) {
              return getMachinsByItemId(id);
            }
          });

  @Override
  public List<XhfMachinePropertyDO> getListByMachineId(Long id) {
    return xhfMachinePropertyMapper.getListByMachineId(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteByMachineId(Long i) {
    xhfMachinePropertyMapper.delete(XhfMachinePropertyDO::getMachineId, i);
  }

  @Override
  @Transactional
  public void delete(List<Long> ids) {
    xhfMachinePropertyMapper.deleteBatchIds(ids);
  }

  @Override
  public Map<Integer, ProductMachinesDTO> getMachins(Long itemId) {
    return clientCache.getUnchecked(itemId);
  }

  private Map<Integer, ProductMachinesDTO> getMachinsByItemId(Long id) {
    Map<Integer, ProductMachinesDTO> result = Maps.newHashMap();
    return result;
  }
}
