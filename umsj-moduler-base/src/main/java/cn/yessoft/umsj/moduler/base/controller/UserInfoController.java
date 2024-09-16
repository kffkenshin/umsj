package cn.yessoft.umsj.moduler.base.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.moduler.base.controller.vo.ChangePwdReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.UpdateUserInfoReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.UserInfoRespVO;
import cn.yessoft.umsj.moduler.base.entity.dto.UserMenuDTO;
import cn.yessoft.umsj.moduler.base.service.IUserInfoService;
import cn.yessoft.umsj.security.core.util.SecurityFrameworkUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import static cn.yessoft.umsj.common.exception.util.ServiceExceptionUtil.exception;
import static cn.yessoft.umsj.common.pojo.ApiResult.success;
import static cn.yessoft.umsj.moduler.base.enums.ErrorCodeConstants.FILE_NOT_EXISTS;


@RestController
@RequestMapping("/api/base")
@Validated
public class UserInfoController {

    @Resource
    private IUserInfoService userInfoService;

    @GetMapping("/userinfo")
    public ApiResult<UserInfoRespVO> getUserInfo() {
        return success(userInfoService.getUserInfo());
    }

    @PostMapping("/change-pwd")
    public ApiResult<String> changePwd(@RequestBody @Valid ChangePwdReqVO changePwd) {
        userInfoService.changePwd(changePwd);
        return success();
    }

    @PostMapping("/update-uinfo")
    public ApiResult<String> updateUserinfo(@RequestBody @Valid UpdateUserInfoReqVO userInfo) {
        userInfoService.updateUserinfo(userInfo);
        return success();
    }

    @PostMapping(value = "/update-avatar")
    public ApiResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_NOT_EXISTS);
        }
        String avatar = userInfoService.updateUserAvatar(file.getInputStream());
        return success(avatar);
    }

    @GetMapping("/user-menu")
    public ApiResult<List<UserMenuDTO>> getUserMenu() {
        return success(userInfoService.getUserMenu());
    }

    @GetMapping("/user-permissions")
    public ApiResult<Set<String>> getUserPermissions() {
        return success(SecurityFrameworkUtils.getLoginUserPermissions());
    }
}
