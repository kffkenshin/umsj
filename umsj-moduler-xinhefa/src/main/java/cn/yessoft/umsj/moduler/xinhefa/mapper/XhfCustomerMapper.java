package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;

import java.util.List;

/**
 * <p>
 * 客户信息表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
public interface XhfCustomerMapper extends YesBaseMapper<XhfCustomerDO> {

    default PageResult<XhfCustomerDO> pagedQuery(CustomerQueryReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<XhfCustomerDO>()
                .likeIfPresent(XhfCustomerDO::getNo, reqVO.getInfo())
                .or().likeIfPresent(XhfCustomerDO::getNameCn, reqVO.getInfo())
                .or().likeIfPresent(XhfCustomerDO::getNameShort, reqVO.getInfo())
                .or().likeIfPresent(XhfCustomerDO::getCusGroup, reqVO.getInfo())
        );
    }

    default List<XhfCustomerDO> listQuery(CustomerQueryReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<XhfCustomerDO>()
                .likeIfPresent(XhfCustomerDO::getNo, reqVO.getInfo())
                .or().likeIfPresent(XhfCustomerDO::getNameCn, reqVO.getInfo())
                .or().likeIfPresent(XhfCustomerDO::getNameShort, reqVO.getInfo())
                .or().likeIfPresent(XhfCustomerDO::getCusGroup, reqVO.getInfo())
        );
    }

    ;;
}
