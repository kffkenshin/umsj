package cn.yessoft.umsj.moduler.base.controller.vo;

import cn.yessoft.umsj.common.pojo.PageParam;
import lombok.Data;

/**
 * 通用 只有一个 info的 分页查询VO
 */
@Data
public class InfoPageQueryReqVO extends PageParam {

    private String info;

}
