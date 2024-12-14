package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.common.utils.CacheUtils.buildAsyncReloadingCache;
import static cn.yessoft.umsj.moduler.xinhefa.enums.XHFErrorCodeConstants.MO_MACHINE_IS_EMPTY;
import static cn.yessoft.umsj.moduler.xinhefa.enums.XHFErrorCodeConstants.NO_MACHINE_SPEED;

import cn.hutool.core.bean.BeanUtil;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.xinhefa.entity.*;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.*;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFWorkStationEnum;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfMachinePropertyMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.*;
import cn.yessoft.umsj.moduler.xinhefa.utils.MachineRuleUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.apache.commons.compress.utils.Lists;
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
  @Resource private IXhfMachineService xhfMachineService;
  @Resource private IXhfProductProcessRouteService xhfProductProcessRouteService;
  @Resource private IXhfItemService xhfItemService;
  @Resource private IXhfProductBomService xhfProductBomService;
  @Resource private IXhfCustomerService xhfCustomerService;

  @Getter
  private final LoadingCache<MachineChoiceDTO, Map<String, ProductMachinesDTO>> clientCache =
      buildAsyncReloadingCache(
          Duration.ofSeconds(100L),
          new CacheLoader<MachineChoiceDTO, Map<String, ProductMachinesDTO>>() {
            @Override
            public Map<String, ProductMachinesDTO> load(MachineChoiceDTO ids) {
              return getMachinesByIds(ids);
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
  public Map<String, ProductMachinesDTO> getMachines(MachineChoiceDTO itemId) {
    return clientCache.getUnchecked(itemId);
  }

  private Map<String, ProductMachinesDTO> getMachinesByIds(MachineChoiceDTO ids) {
    Map<String, ProductMachinesDTO> result = Maps.newHashMap();
    List<SimulateDetailDTO> routes = xhfProductProcessRouteService.listByitemId(ids.getItemId());
    Set<Integer> ws = Sets.newLinkedHashSet();
    for (SimulateDetailDTO i : routes) {
      ws.add(i.getWorkStation());
    }
    XhfItemDO item = xhfItemService.getById(ids.getItemId());
    List<XhfProductBomDTO> bom = xhfProductBomService.getByItemId(ids.getItemId());
    XhfCustomerDO customer = xhfCustomerService.getById(ids.getCustomerId());
    MachineChosenDTO params = xhfMachineService.getMachineChosen();
    ProductMachinesDTO zdx = null;
    for (Integer i : ws) {
      switch (XHFWorkStationEnum.valueOf(i)) {
        case YS -> { // 选机标准选机
          ProductMachinesDTO m = pickStatic(item, params);
          fillMachineParams(m, item, bom, customer, routes, params);
          result.put(BaseUtils.toString(i), m);
        }
        case PM, JM, KM, FH, TJ -> { // 区分规则选机
          ProductMachinesDTO m = pickDynamic(i, item, bom, null, routes, customer, params);
          fillMachineParams(m, item, bom, customer, routes, params);
          result.put(BaseUtils.toString(i), m);
        }
        case ZDX -> { // 区分规则选机
          ProductMachinesDTO m = pickDynamic(i, item, bom, null, routes, customer, params);
          fillMachineParams(m, item, bom, customer, routes, params);
          zdx = m;
          result.put(BaseUtils.toString(i), m);
        }
        case ZDJ -> { // 区分规则选机
          if (zdx != null) {
            if (zdx.getFirstMachine() != null) {
              ProductMachinesDTO m =
                  pickDynamic(
                      i, item, bom, zdx.getFirstMachine().getMachineNo(), routes, customer, params);
              fillMachineParams(m, item, bom, customer, routes, params);
              result.put(
                  BaseUtils.toString(i).concat("-").concat(zdx.getFirstMachine().getMachineNo()),
                  m);
            }
            if (!zdx.getSecondMachines().isEmpty()) {
              for (MachineParamsDTO ezdx : zdx.getSecondMachines()) {
                ProductMachinesDTO m =
                    pickDynamic(i, item, bom, ezdx.getMachineNo(), routes, customer, params);
                fillMachineParams(m, item, bom, customer, routes, params);
                result.put(BaseUtils.toString(i).concat("-").concat(ezdx.getMachineNo()), m);
              }
            }
          } else {
            ProductMachinesDTO m = pickDynamic(i, item, bom, null, routes, customer, params);
            fillMachineParams(m, item, bom, customer, routes, params);
            result.put(BaseUtils.toString(i), m);
          }
        }
      }
    }
    return result;
  }

  private ProductMachinesDTO pickStatic(XhfItemDO item, MachineChosenDTO params) {
    if (BaseUtils.isEmpty(item.getFirstMachine())) {
      throw exception(MO_MACHINE_IS_EMPTY, "印刷");
    }
    ProductMachinesDTO dto = new ProductMachinesDTO();
    dto.setDefaultNumber(1);
    MachineParamsDTO mdto = new MachineParamsDTO();
    mdto.setMachineNo(item.getFirstMachine());
    dto.setFirstMachine(mdto);
    dto.setSecondMachines(Lists.newArrayList());
    return dto;
  }

  private void fillMachineParams(
      ProductMachinesDTO m,
      XhfItemDO item,
      List<XhfProductBomDTO> bom,
      XhfCustomerDO customer,
      List<SimulateDetailDTO> routes,
      MachineChosenDTO params) {
    boolean found = false;
    for (XhfMachinePropertyDO e : params.getMachineProperties()) {
      if (BaseUtils.isNotEmpty(m.getFirstMachine())
          && MachineRuleUtil.isFit(
              m.getFirstMachine().getMachineNo(),
              item,
              bom,
              customer,
              routes,
              e,
              params.getMachines())) {
        m.setRollLength(e.getRollLength());
        BeanUtil.copyProperties(e, m.getFirstMachine());
        found = true;
        break;
      }
      if (BaseUtils.isNotEmpty(m.getSecondMachines())) {
        for (MachineParamsDTO b : m.getSecondMachines()) {
          if (MachineRuleUtil.isFit(
              b.getMachineNo(), item, bom, customer, routes, e, params.getMachines())) {
            m.setRollLength(e.getRollLength());
            BeanUtil.copyProperties(e, b);
            found = true;
          }
        }
      }
    }
    if (!found) {
      throw exception(NO_MACHINE_SPEED);
    }
  }

  private ProductMachinesDTO pickDynamic(
      Integer workStation,
      XhfItemDO item,
      List<XhfProductBomDTO> bom,
      String xdxNo,
      List<SimulateDetailDTO> routes,
      XhfCustomerDO customer,
      MachineChosenDTO params) {
    for (XhfDynamicMachineDeterminantDO e : params.getDynamicMachineDeterminants()) {
      if (MachineRuleUtil.isFit(workStation, e, item, bom, xdxNo, routes, customer)) {
        ProductMachinesDTO dto = new ProductMachinesDTO();
        dto.setDefaultNumber(e.getDefaultMachineCount());
        MachineParamsDTO mdto = new MachineParamsDTO();
        mdto.setMachineNo(e.getFirstMachineNo());
        dto.setFirstMachine(mdto);
        List<MachineParamsDTO> smdto = Lists.newArrayList();
        if (BaseUtils.isNotEmpty(e.getSencondMachineNo1())) {
          MachineParamsDTO sdto = new MachineParamsDTO();
          sdto.setMachineNo(e.getSencondMachineNo1());
        }
        dto.setSecondMachines(smdto);
        return dto;
      }
    }
    throw exception(MO_MACHINE_IS_EMPTY, XHFWorkStationEnum.valueOf(workStation).getName());
  }
}
