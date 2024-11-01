package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.InfoPageQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ProductProcessVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.RejectRateSimulateVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductProcessDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.RejectRateSimulateDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;

/**
 * 生产工艺表 服务类
 *
 * @author ethan
 * @since 2024-09-20
 */
public interface IXhfProductProcessService extends IService<XhfProductProcessDO> {

  PageResult<XhfProductProcessDO> pagedQuery(InfoPageQueryReqVO reqVO);

  void updateRates(ProductProcessVO reqVO);

  RejectRateSimulateDTO simulate(RejectRateSimulateVO reqVO);

  RejectRateSimulateDTO simulate(String itemNo, BigDecimal qty);

  RejectRateSimulateDTO simulate(XhfItemDO item, BigDecimal qty);
}
