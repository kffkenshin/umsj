package cn.yessoft.umsj.security.service;

/**
 * Security 框架 Service 接口，定义权限相关的校验操作
 */
public interface ISecurityFrameworkService {

    /**
     * 判断是否有权限
     *
     * @param permission 权限
     * @return 是否
     */
    boolean hasPermission(String permission);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(String... permissions);

}
