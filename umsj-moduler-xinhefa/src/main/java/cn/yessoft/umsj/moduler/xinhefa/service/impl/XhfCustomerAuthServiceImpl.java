package cn.yessoft.umsj.moduler.xinhefa.service.impl;

import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerAuthVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerAuthDO;
import cn.yessoft.umsj.moduler.xinhefa.mapper.XhfCustomerAuthMapper;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_DUPLICATE;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

/**
 * <p>
 * 客服权限表 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
@Service("xhfCustomerAuthService")
public class XhfCustomerAuthServiceImpl extends ServiceImpl<XhfCustomerAuthMapper, XhfCustomerAuthDO> implements IXhfCustomerAuthService {

    @Resource
    private XhfCustomerAuthMapper xhfCustomerAuthMapper;


    @Override
    public List<XhfCustomerAuthDO> queryAuthList(CustomerQueryReqVO reqVO) {
        return xhfCustomerAuthMapper.queryAuthList(reqVO);
    }

    @Override
    public void delete(List<Long> ids) {
        xhfCustomerAuthMapper.deleteBatchIds(ids);
    }

    @Override
    public void create(CustomerAuthVO reqVO) {
        // 验证唯一
        validateDuplicate(reqVO.getCustomerId(), reqVO.getAccountId());
        xhfCustomerAuthMapper.insert(BeanUtils.toBean(reqVO, XhfCustomerAuthDO.class));

    }

    private void validateDuplicate(Long customerId, Long accountId) {
        XhfCustomerAuthDO xhfCustomerAuthDO = xhfCustomerAuthMapper.getByCusIdAndAccountId(customerId, accountId);
        if (xhfCustomerAuthDO != null) {
            throw exception(O_DUPLICATE, "客服权限");
        }
    }

    @Override
    public void update(CustomerAuthVO reqVO) {
        XhfCustomerAuthDO r = validateExist(reqVO.getId());
        xhfCustomerAuthMapper.updateById(BeanUtils.toBean(reqVO, XhfCustomerAuthDO.class));
    }

    @Override
    public XhfCustomerAuthDO validateExist(Long id) {
        XhfCustomerAuthDO xhfCustomerAuthDO = xhfCustomerAuthMapper.selectById(id);
        if (xhfCustomerAuthDO == null) {
            throw exception(O_NOT_EXISTS, "客服权限");
        }
        return xhfCustomerAuthDO;
    }
}
