package cn.yessoft.umsj.moduler.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.yessoft.umsj.moduler.base.controller.vo.ChangePwdReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.UpdateUserInfoReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.UserInfoRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseAccountDO;
import cn.yessoft.umsj.moduler.base.entity.dto.UserMenuDTO;
import cn.yessoft.umsj.moduler.base.enums.BaseConstants;
import cn.yessoft.umsj.moduler.base.service.IBaseAccountService;
import cn.yessoft.umsj.moduler.base.service.IBaseFileService;
import cn.yessoft.umsj.moduler.base.service.IBaseMenuService;
import cn.yessoft.umsj.moduler.base.service.IUserInfoService;
import cn.yessoft.umsj.security.core.util.SecurityFrameworkUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.PASSWORD_WRONG;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.USER_NOT_EXISTS;


/**
 * Auth Service 实现类
 */
@Service("userInfoService")
@Slf4j
public class UserInfoServiceImpl implements IUserInfoService {

    @Resource
    private IBaseAccountService baseAccountService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IBaseFileService baseFileService;

    @Resource
    private IBaseMenuService baseMenuService;


    @Override
    public UserInfoRespVO getUserInfo() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        if (userId == null) {
            throw exception(USER_NOT_EXISTS);
        }
        UserInfoRespVO r = BeanUtil.copyProperties(baseAccountService.getById(userId), UserInfoRespVO.class);
        r.setPermissions(SecurityFrameworkUtils.getLoginUserPermissions());
        return r;
    }

    @Override
    @Transactional
    public void changePwd(ChangePwdReqVO changePwd) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        BaseAccountDO account = validateUserExists(userId);
        if (!baseAccountService.isPasswordMatch(changePwd.getOldPassword(), account.getPassword())) {
            throw exception(PASSWORD_WRONG);
        }
        account.setPassword(passwordEncoder.encode(changePwd.getPassword()));
        baseAccountService.updateById(account);
    }

    @Override
    @Transactional
    public void updateUserinfo(UpdateUserInfoReqVO userInfo) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        BaseAccountDO account = validateUserExists(userId);
        account.setEmail(userInfo.getEmail());
        account.setNickName(userInfo.getNickName());
        baseAccountService.updateById(account);
    }

    @Override
    @Transactional
    public String updateUserAvatar(InputStream avatarFile) {

        Long userId = SecurityFrameworkUtils.getLoginUserId();
        BaseAccountDO account = validateUserExists(userId);
        // 存储文件
        String avatar = baseFileService.createFile(IoUtil.readBytes(avatarFile), BaseAccountDO.class, account.getId());
        // 更新路径
        account.setAvatar(avatar);
        baseAccountService.updateById(account);
        return avatar;
    }

    @Override
    public List<UserMenuDTO> getUserMenu() {
        Set<String> permissions = SecurityFrameworkUtils.getLoginUserPermissions();
        // 没权限的话 给个值
        if (CollectionUtils.isEmpty(permissions)) {
            permissions.add("WE#@%522polPPs");
        }
        return baseMenuService.getMenuByPermissions(permissions, BaseConstants.ROOT_MENU_ID);
    }

    private BaseAccountDO validateUserExists(Long id) {
        if (id == null) {
            throw exception(USER_NOT_EXISTS);
        }
        BaseAccountDO account = baseAccountService.getById(id);
        if (account == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return account;
    }

}
