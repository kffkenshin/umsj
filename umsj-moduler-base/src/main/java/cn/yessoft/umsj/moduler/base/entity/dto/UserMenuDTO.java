package cn.yessoft.umsj.moduler.base.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class UserMenuDTO {

    @JsonIgnore
    private Long id;

    @JSONField(ordinal = 1)
    private String path;

    @JSONField(ordinal = 2)
    private String name;

    @JSONField(ordinal = 3)
    private UserMenuMetaDTO meta;

    @JSONField(ordinal = 4)
    private List<UserMenuDTO> children;

}
