package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.menu.MenuQueryReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseMenuDO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseMenuDTO;
import cn.yessoft.umsj.moduler.base.entity.dto.BaseRolePermissionDTO;
import cn.yessoft.umsj.moduler.base.entity.dto.UserMenuDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单及功能表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-08-31
 */
public interface IBaseMenuService extends IService<BaseMenuDO> {

    PageResult<BaseMenuDTO> pagedQuery(MenuQueryReqVO reqVO);

    BaseMenuDO getMenu(Long id);

    void switchEnable(String id);

    List<BaseRolePermissionDTO> listByRoleIDAndParentId(Long roleId, Long parentId);

    List<UserMenuDTO> getMenuByPermissions(Set<String> permissions, Long rootMenuId);
}
