package cn.yessoft.umsj.moduler.base.controller.vo.role;

import cn.yessoft.umsj.common.pojo.PageParam;
import lombok.Data;

@Data
public class RoleQueryReqVO extends PageParam {

    private String info;

    private Integer roleId;

}
