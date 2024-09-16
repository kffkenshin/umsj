package cn.yessoft.umsj.moduler.base.entity.dto;

import lombok.Data;

@Data
public class UserMenuMetaDTO {

    private String locale;

    private boolean requiresAuth = true;

    private String icon;

    private Integer order;
}
