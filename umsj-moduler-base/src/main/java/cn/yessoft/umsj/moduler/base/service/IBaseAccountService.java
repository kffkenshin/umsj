package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.account.AccountQueryReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.account.BaseAccountRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseAccountDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-04
 */
public interface IBaseAccountService extends IService<BaseAccountDO> {

    boolean isPasswordMatch(String rawPassword, String encodedPassword);

    BaseAccountDO getByAid(String aid);

    PageResult<BaseAccountDO> pagedQuery(AccountQueryReqVO reqVO);

    List<BaseAccountDO> listQuery(AccountQueryReqVO reqVO);

    int create(BaseAccountRespVO reqVO);

    void update(BaseAccountRespVO reqVO);

    BaseAccountDO get(Long id);

    void delete(List<Long> ids);

    void initPassword(List<Long> ids);
}
