package cn.yessoft.umsj.moduler.base.controller;

import cn.hutool.core.util.StrUtil;
import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.moduler.base.controller.vo.LoginReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.LoginRespVO;
import cn.yessoft.umsj.moduler.base.service.ISystemAuthService;
import cn.yessoft.umsj.security.config.SecurityProperties;
import cn.yessoft.umsj.security.core.util.SecurityFrameworkUtils;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;


@RestController
@RequestMapping("/api/base")
@Validated
public class LoginController {

    @Resource
    private ISystemAuthService systemAuthService;

    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/login")
    @PermitAll
    public ApiResult<LoginRespVO> login(@RequestBody @Valid LoginReqVO reqVO) {
        return success(systemAuthService.login(reqVO));
    }

    @PostMapping("/logout")
    @PermitAll
    public ApiResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            systemAuthService.logout(token);
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    public ApiResult<LoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(systemAuthService.refreshToken(refreshToken));
    }
}
