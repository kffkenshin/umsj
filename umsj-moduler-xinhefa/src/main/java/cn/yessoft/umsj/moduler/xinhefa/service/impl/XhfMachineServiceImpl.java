package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.utils.CacheUtils.buildAsyncReloadingCache;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachineDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.MachineChosenDTO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfMachineMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfDynamicMachineDeterminantService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachinePropertyService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfMachineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Service;

/**
 * 机台表 服务实现类
 *
 * @author ethan
 * @since 2024-10-17
 */
@Service("xhfMachineService")
public class XhfMachineServiceImpl extends ServiceImpl<XhfMachineMapper, XhfMachineDO>
    implements IXhfMachineService {
  @Resource private XhfMachineMapper xhfMachineMapper;
  @Resource private IXhfMachinePropertyService xhfMachinePropertyService;
  @Resource private IXhfDynamicMachineDeterminantService xhfDynamicMachineDeterminantService;
  private static Long CAHCE_ID = 1L;

  @Getter
  private final LoadingCache<Long, MachineChosenDTO> clientCache =
      buildAsyncReloadingCache(
          Duration.ofSeconds(100L),
          new CacheLoader<Long, MachineChosenDTO>() {
            @Override
            public MachineChosenDTO load(Long id) {
              return prepareMachineChosenDTO();
            }
          });

  private MachineChosenDTO prepareMachineChosenDTO() {
    MachineChosenDTO machineChosenDTO = new MachineChosenDTO();
    // 机台
    Map<String, XhfMachineDO> machines = new HashMap<>();
    List<XhfMachineDO> machineList = xhfMachineMapper.selectList();
    machineList.forEach(
        i -> {
          machines.put(i.getMachineNo(), i);
        });
    machineChosenDTO.setMachines(machines);
    // 机台参数
    machineChosenDTO.setMachineProperties(xhfMachinePropertyService.list());
    // 选机
    machineChosenDTO.setDynamicMachineDeterminants(xhfDynamicMachineDeterminantService.list());
    return machineChosenDTO;
  }

  @Override
  public PageResult<XhfMachineDO> pagedQuery(InfoPageQueryReqVO reqVO) {
    return xhfMachineMapper.pagedQuery(reqVO);
  }

  @Override
  public void delete(List<Long> ids) {
    ids.forEach(
        i -> {
          XhfMachineDO machine = xhfMachineMapper.selectById(i);
          if (machine != null) {
            xhfMachinePropertyService.deleteByMachineId(i);
            xhfMachineMapper.deleteById(i);
          }
        });
  }

  @Override
  public MachineChosenDTO getMachineChosen() {
    return clientCache.getUnchecked(CAHCE_ID);
  }
}
