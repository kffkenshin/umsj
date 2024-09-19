package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerAuthDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * <p>
 * 客服权限表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
public interface XhfCustomerAuthMapper extends YesBaseMapper<XhfCustomerAuthDO> {

    default List<XhfCustomerAuthDO> queryAuthList(CustomerQueryReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<XhfCustomerAuthDO>()
                .eq(XhfCustomerAuthDO::getCustomerId, reqVO.getCustomerId())
        );
    }

    default XhfCustomerAuthDO getByCusIdAndAccountId(Long customerId, Long accountId) {
        return selectOne(new LambdaQueryWrapper<XhfCustomerAuthDO>().eq(XhfCustomerAuthDO::getCustomerId, customerId)
                .eq(XhfCustomerAuthDO::getAccountId, accountId));
    }

    ;;
}
