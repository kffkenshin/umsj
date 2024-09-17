package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemTypeQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemTypeRespVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemTypeDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品类型表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
public interface IXhfItemTypeService extends IService<XhfItemTypeDO> {

    PageResult<XhfItemTypeDO> pagedQuery(ItemTypeQueryReqVO reqVO);

    List<XhfItemTypeDO> listQuery(ItemTypeQueryReqVO reqVO);

    XhfItemTypeDO validateExist(Long id);

    void delete(List<Long> ids);

    XhfItemTypeDO getByTypeAndName(String typeClass, String name);

    void update(ItemTypeRespVO reqVO);

    void create(ItemTypeRespVO reqVO);
}
