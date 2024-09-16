package cn.yessoft.umsj.security.service;

import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static cn.yessoft.umsj.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.yessoft.umsj.security.core.util.SecurityFrameworkUtils.getLoginUserPermissions;

@AllArgsConstructor
public class SecurityFrameworkServiceImpl implements ISecurityFrameworkService {

    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    @Override
    public boolean hasAnyPermissions(String... permissions) {
        Long userId = getLoginUserId();
        if (userId == null) {
            return false;
        }
        Set<String> _permissions = getLoginUserPermissions();
        if (CollectionUtils.isEmpty(_permissions)) {
            return false;
        }
        return CollectionUtils.containsAny(_permissions, List.of(permissions));
    }
}
