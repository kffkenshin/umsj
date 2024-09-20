package cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 生产工艺表
 * </p>
 *
 * @author ethan
 * @since 2024-09-20
 */
@Getter
@Setter
public class ProductProcessVO {


    private Long id;

    /**
     * 工艺编号
     */
    private String processNo;

    /**
     * 名称
     */
    private String processName;

    /**
     * 所属工段
     */
    private Integer workStation;

    private String workStationStr;
}
