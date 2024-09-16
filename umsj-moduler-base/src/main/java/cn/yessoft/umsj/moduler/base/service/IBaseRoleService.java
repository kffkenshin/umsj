package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleQueryReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.role.RoleRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseRoleDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-10
 */
public interface IBaseRoleService extends IService<BaseRoleDO> {

    PageResult<BaseRoleDO> pagedQuery(RoleQueryReqVO reqVO);

    void delete(List<Long> ids);

    void create(RoleRespVO reqVO);

    BaseRoleDO getByNo(String roleNo);

    BaseRoleDO validateExist(Long id);

    void update(RoleRespVO reqVO);

    List<BaseRoleDO> listQuery(RoleQueryReqVO reqVO);

    Set<Long> getAllByAccountID(Long accountID);

    List<String> getRoleNosByIds(Set<Long> roleIds);
}
