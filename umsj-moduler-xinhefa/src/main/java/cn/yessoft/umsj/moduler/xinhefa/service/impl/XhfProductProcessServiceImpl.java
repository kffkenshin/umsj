package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BaseUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ProductProcessVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.RejectRateSimulateVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.RejectRateSimulateDTO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.SimulateDetailDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFWorkStationEnum;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfProductProcessMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfItemService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductProcessRouteService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductProcessService;
import cn.yessoft.umsj.moduler.xinhefa.utils.XHFUtils;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 生产工艺表 服务实现类
 *
 * @author ethan
 * @since 2024-09-20
 */
@Service("xhfProductProcessService")
public class XhfProductProcessServiceImpl
    extends ServiceImpl<XhfProductProcessMapper, XhfProductProcessDO>
    implements IXhfProductProcessService {

  @Resource private XhfProductProcessMapper xhfProductProcessMapper;
  @Resource private IXhfItemService xhfItemService;
  @Resource private IXhfProductProcessRouteService xhfProductProcessRouteService;

  @Override
  public PageResult<XhfProductProcessDO> pagedQuery(InfoPageQueryReqVO reqVO) {
    return xhfProductProcessMapper.selectPage(
        reqVO, new LambdaQueryWrapperX<XhfProductProcessDO>());
  }

  @Override
  public void updateRates(ProductProcessVO reqVO) {
    XhfProductProcessDO r = xhfProductProcessMapper.selectById(reqVO.getId());
    if (r != null) {
      if (reqVO.getAbnormalRejectRate() != null)
        r.setAbnormalRejectRate(reqVO.getAbnormalRejectRate());
      if (reqVO.getProcessRejectRate() != null)
        r.setProcessRejectRate(reqVO.getProcessRejectRate());
      if (reqVO.getPrepareRejectRate() != null)
        r.setPrepareRejectRate(reqVO.getPrepareRejectRate());
      xhfProductProcessMapper.updateById(r);
    }
  }

  @Override
  public RejectRateSimulateDTO simulate(RejectRateSimulateVO reqVO) {
    XhfItemDO item = xhfItemService.getItemByNo(reqVO.getItemNo());
    return simulate(item, reqVO.getQty());
  }

  @Override
  public RejectRateSimulateDTO simulate(String itemNo, BigDecimal qty) {
    RejectRateSimulateVO vo = new RejectRateSimulateVO();
    vo.setItemNo(itemNo);
    vo.setQty(qty);
    return simulate(vo);
  }

  @Override
  public RejectRateSimulateDTO simulate(XhfItemDO item, BigDecimal qty) {
    RejectRateSimulateDTO result = new RejectRateSimulateDTO();
    BigDecimal difficulty = BigDecimal.ONE;
    if (item == null) {
      throw exception(O_NOT_EXISTS, "料号");
    }
    BigDecimal channelsCount =
        new BigDecimal(item.getChannelsCount() == null ? 1 : item.getChannelsCount());
    result.addValue("产内料号", item.getItemNo());
    result.addValue("订单数量", qty.toPlainString());
    result.addValue("产品品名", item.getItemName());
    result.addValue("销售单位", item.getSaleUnit());
    result.addValue("色数", BaseUtils.toString(item.getColorCount()));
    result.addValue("宽度", BaseUtils.toString(item.getWidth()));
    result.addValue("袋长", BaseUtils.toString(item.getLength()));
    result.addValue("通道数", BaseUtils.toString(item.getChannelsCount()));
    result.addValue("克重", BaseUtils.toString(item.getGWeight()));
    XHFUtils.ProductUnitConvert units = XHFUtils.getProductUnitConvert(item, qty);
    BigDecimal cpms = units.getM().divide(XHFUtils.K);
    BigDecimal fcpms = units.getM().divide(XHFUtils.K);
    if (fcpms.compareTo(BigDecimal.ZERO) == 0) {
      throw exception(O_NOT_EXISTS, item.getItemNo() + "成品米数");
    }
    result.addValue("成品米数", cpms.toPlainString());
    List<SimulateDetailDTO> routes = xhfProductProcessRouteService.listByitemId(item.getId());
    result.setDetail(routes);
    if (routes.isEmpty()) {
      throw exception(O_NOT_EXISTS, "工艺路线");
    }
    BigDecimal actAbnormalRejectRate = new BigDecimal(0);
    boolean hasSum = false;
    boolean afterFQ = false;
    for (SimulateDetailDTO each : routes) {
      if (BaseUtils.isNotEmpty(each.getWorkStation()))
        each.setWorkStationStr(XHFWorkStationEnum.valueOf(each.getWorkStation()).getName());
      // 印刷准备米数
      if ("YS".equals(each.getProcessNo()) || "RY".equals(each.getProcessNo())) {
        // 100+(色数-1)*60
        each.setPrepareQty1(XHFUtils.B.add(new BigDecimal((item.getColorCount() - 1) * 60)));
      }
      // 异常废品率在成型累加扣除
      if ("CX".equals(each.getProcessNo())) {
        each.setActAbnormalRejectRate(actAbnormalRejectRate.add(each.getAbnormalRejectRate()));
        hasSum = true;
      } else if (hasSum) {
        // 成型后的
        each.setActAbnormalRejectRate(each.getAbnormalRejectRate());
      } else {
        each.setActAbnormalRejectRate(BigDecimal.ZERO);
      }
      actAbnormalRejectRate = actAbnormalRejectRate.add(each.getAbnormalRejectRate());
      // 分切前后的准备米数
      if ("FQ".equals(each.getProcessNo())) {
        afterFQ = true;
      }
      if (afterFQ) {
        each.setPrepareQty2(each.getPrepareQty1().multiply(channelsCount));
      } else {
        each.setPrepareQty2(each.getPrepareQty1());
      }
    }
    if (!hasSum) {
      routes.get(routes.size() - 1).setActAbnormalRejectRate(actAbnormalRejectRate);
    }
    // 倒过来
    Collections.reverse(routes);
    for (SimulateDetailDTO each : routes) {
      each.setOutputQty(cpms);
      // 不考虑在成型扣除异常废品的投入1
      BigDecimal tOut = cpms.add(each.getPrepareQty2().multiply(difficulty));
      BigDecimal processRate = each.getProcessRejectRate().divide(XHFUtils.B).multiply(difficulty);
      BigDecimal abnormalRejectRate =
          each.getAbnormalRejectRate().divide(XHFUtils.B).multiply(difficulty);
      BigDecimal tRate = BigDecimal.ONE.subtract(processRate).subtract(abnormalRejectRate);
      each.setInputQty1(tOut.divide(tRate, 6, RoundingMode.HALF_UP));
      each.setProcessRejectQty(each.getInputQty1().multiply(processRate));
      each.setAbnormalRejectQty(each.getInputQty1().multiply(abnormalRejectRate));
      each.setActAbnormalRejectQty(
          each.getInputQty1()
              .multiply(each.getActAbnormalRejectRate().divide(XHFUtils.B).multiply(difficulty)));
      // 考虑在成型扣除异常废品的投入2
      each.setInputQty2(
          each.getOutputQty()
              .add(each.getPrepareQty2())
              .add(each.getProcessRejectQty())
              .add(each.getActAbnormalRejectQty()));
      cpms = each.getInputQty2();
    }
    // 再倒过来
    Collections.reverse(routes);
    afterFQ = false;
    for (SimulateDetailDTO each : routes) {
      if ("FQ".equals(each.getProcessNo())) {
        afterFQ = true;
        each.setOutputQty(each.getOutputQty().multiply(channelsCount));
        continue;
      }
      if (afterFQ) {
        each.setOutputQty(each.getOutputQty().multiply(channelsCount));
        each.setInputQty1(each.getInputQty1().multiply(channelsCount));
        each.setInputQty2(each.getInputQty2().multiply(channelsCount));
      }
    }
    // 最后一道是制袋机
    routes.get(routes.size() - 1).setWorkStation(XHFWorkStationEnum.ZDJ.getCode());
    // 表头
    result.addValue("额外附加难度", difficulty.setScale(2, RoundingMode.HALF_DOWN) + "");
    result.addValue("投料米数", routes.get(0).getInputQty1().setScale(3, RoundingMode.HALF_DOWN) + "");
    BigDecimal diff = routes.get(0).getInputQty1().subtract(fcpms);
    result.addValue(
        "放量%",
        diff.divide(fcpms, 6, RoundingMode.HALF_UP)
                .multiply(XHFUtils.B)
                .setScale(2, RoundingMode.HALF_DOWN)
            + "%");
    result.addValue(
        "放量",
        diff.divide(fcpms, 6, RoundingMode.HALF_UP)
            .setScale(2, RoundingMode.HALF_DOWN)
            .toPlainString());
    return result;
  }
}
