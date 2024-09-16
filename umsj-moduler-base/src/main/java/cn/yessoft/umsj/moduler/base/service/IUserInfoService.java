package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.moduler.base.controller.vo.ChangePwdReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.UpdateUserInfoReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.UserInfoRespVO;
import cn.yessoft.umsj.moduler.base.entity.dto.UserMenuDTO;

import java.io.InputStream;
import java.util.List;

/**
 * 用户信息
 */
public interface IUserInfoService {

    /**
     * 获取用户信息
     */
    UserInfoRespVO getUserInfo();

    void changePwd(ChangePwdReqVO changePwd);

    void updateUserinfo(UpdateUserInfoReqVO userInfo);

    String updateUserAvatar(InputStream inputStream);

    List<UserMenuDTO> getUserMenu();
}
