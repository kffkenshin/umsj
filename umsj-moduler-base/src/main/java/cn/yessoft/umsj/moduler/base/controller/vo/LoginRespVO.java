package cn.yessoft.umsj.moduler.base.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespVO {

    private String aid;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime expiresTime;

}
