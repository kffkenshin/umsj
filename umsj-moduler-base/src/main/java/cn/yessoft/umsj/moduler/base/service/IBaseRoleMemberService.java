package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleMemberCreateVO;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleQueryReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseRoleMemberDO;
import cn.yessoft.umsj.moduler.base.entity.dto.IdAndNameDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色成员表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-10
 */
public interface IBaseRoleMemberService extends IService<BaseRoleMemberDO> {

    PageResult<BaseRoleMemberDO> pagedQuery(RoleQueryReqVO reqVO);

    void delete(List<Long> ids);

    void deleteByRoleId(Long i);

    void create(RoleMemberCreateVO reqVO);

    BaseRoleMemberDO validateExist(Long id);

    List<Long> getRoleIdByAccountId(Long accountID);

    Set<Long> getAllByRoleId(Long roleId, Set<Long> selectedIds);

    List<IdAndNameDTO> listRoleAccount(Long roleId);
}
