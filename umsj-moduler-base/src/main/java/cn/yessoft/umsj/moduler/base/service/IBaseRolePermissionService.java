package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.moduler.base.controller.vo.rolepermission.RolePemissionSwitchReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseRolePermissionDO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseRolePermissionDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-13
 */
public interface IBaseRolePermissionService extends IService<BaseRolePermissionDO> {

    void turnOn(RolePemissionSwitchReqVO reqVO);

    void turnOff(RolePemissionSwitchReqVO reqVO);

    List<BaseRolePermissionDTO> listByRoleIDAndParentId(Long roleId, Long parentId);

    List<String> getPermissionsByRoleIds(Set<Long> roleIds);
}
