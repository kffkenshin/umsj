package cn.yessoft.umsj.moduler.xinhefa.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 工艺路线表
 * </p>
 *
 * @author ethan
 * @since 2024-09-21
 */
@Getter
@Setter
public class XhfProductProcessRouteDTO {


    private Byte seq;

    private String processNo;

    private String processName;

    private Integer workStation;

    private String workStationStr;
}
