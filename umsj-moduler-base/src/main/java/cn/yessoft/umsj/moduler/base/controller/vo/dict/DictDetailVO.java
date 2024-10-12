package cn.yessoft.umsj.moduler.base.controller.vo.dict;

import lombok.Data;

/**
 * <p>
 * 数据字典明细表
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
@Data
public class DictDetailVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 字典id
     */
    private Long dictId;

    /**
     * 顺序号
     */
    private Byte seq;

    /**
     * 明细编号
     */
    private String detailKey;

    /**
     * 明细名称
     */
    private String detailValue;
}
