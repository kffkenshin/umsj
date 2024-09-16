package cn.yessoft.umsj.moduler.base.controller.vo.menu;

import lombok.Data;

@Data
public class MenuRespVO {

    private Long id;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 名称
     */
    private String name;

    /**
     * 权限值
     */
    private String permission;

    /**
     * 菜单类型
     */
    private Integer type;

    /**
     * 上级菜单
     */
    private String parent;

    /**
     * 启用停用
     */
    private Integer enable;

    /**
     * 图标
     */
    private String icon;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 语言包名
     */
    private String locale;

}
