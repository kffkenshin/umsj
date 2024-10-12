package cn.yessoft.umsj.moduler.base.controller.vo.dict;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
@Getter
@Setter
public class DictVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 字典名字
     */
    private String name;

    /**
     * 说明
     */
    private String description;
}
