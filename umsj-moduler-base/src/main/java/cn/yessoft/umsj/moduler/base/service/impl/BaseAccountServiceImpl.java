package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.account.AccountQueryReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.account.BaseAccountRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseAccountDO;
import cn.yessoft.umsj.moduler.base.mapper.BaseAccountMapper;
import cn.yessoft.umsj.moduler.base.service.IBaseAccountService;
import cn.yessoft.umsj.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.AID_DUPLICATE;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.O_NOT_EXISTS;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author ethan
 * @since 2024-09-04
 */
@Service("baseAccountService")
public class BaseAccountServiceImpl extends ServiceImpl<BaseAccountMapper, BaseAccountDO> implements IBaseAccountService {
    @Resource
    private BaseAccountMapper baseAccountMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Value("${yesee.base.default-password}")
    private String DEFAULT_PASSWORD;


    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public BaseAccountDO getByAid(String aid) {
        return baseAccountMapper.selectOne(BaseAccountDO::getAid, aid);
    }

    @Override
    public PageResult<BaseAccountDO> pagedQuery(AccountQueryReqVO reqVO) {
        return baseAccountMapper.selectPage(reqVO, new LambdaQueryWrapperX<BaseAccountDO>().
                likeIfPresent(BaseAccountDO::getAid, reqVO.getInfo())
                .or().likeIfPresent(BaseAccountDO::getNickName, reqVO.getInfo())
                .or().likeIfPresent(BaseAccountDO::getCellPhone, reqVO.getInfo())
                .or().likeIfPresent(BaseAccountDO::getEmail, reqVO.getInfo()));
    }

    @Override
    public List<BaseAccountDO> listQuery(AccountQueryReqVO reqVO) {
        return baseAccountMapper.selectList(new Page<>(0, 100), new LambdaQueryWrapperX<BaseAccountDO>().
                likeIfPresent(BaseAccountDO::getAid, reqVO.getInfo())
                .or().likeIfPresent(BaseAccountDO::getNickName, reqVO.getInfo())
                .or().likeIfPresent(BaseAccountDO::getCellPhone, reqVO.getInfo())
                .or().likeIfPresent(BaseAccountDO::getEmail, reqVO.getInfo()));
    }

    @Override
    public int create(BaseAccountRespVO reqVO) {
        // 验证账号唯一性
        validateAidUnique(reqVO.getAid());
        BaseAccountDO r = BeanUtil.toBean(reqVO, BaseAccountDO.class);
        r.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        return baseAccountMapper.insert(r);
    }

    @Override
    public void update(BaseAccountRespVO reqVO) {
        //校验存在
        BaseAccountDO account = validateExist(reqVO.getId());
        account.setEmail(reqVO.getEmail());
        account.setNickName(reqVO.getNickName());
        account.setCellPhone(reqVO.getCellPhone());
        baseAccountMapper.updateById(account);
    }

    @Override
    public BaseAccountDO get(Long id) {
        return validateExist(id);
    }

    @Override
    public void delete(List<Long> ids) {
        baseAccountMapper.deleteBatchIds(ids);
    }

    @Override
    public void initPassword(List<Long> ids) {
        List<BaseAccountDO> r = baseAccountMapper.selectList("id", ids);
        r.forEach(i -> {
            i.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            baseAccountMapper.updateById(i);
        });
    }

    private void validateAidUnique(String aid) {
        BaseAccountDO account = getByAid(aid);
        if (account != null) {
            throw exception(AID_DUPLICATE);
        }
    }

    private BaseAccountDO validateExist(Long id) {
        BaseAccountDO account = baseAccountMapper.selectById(id);
        if (BeanUtil.isEmpty(account)) {
            throw exception(O_NOT_EXISTS, "账号");
        }
        return account;
    }
}
