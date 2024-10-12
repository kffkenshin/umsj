package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictQueryPageReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
public interface IBaseDictService extends IService<BaseDictDO> {


    PageResult<BaseDictDO> pagedQuery(DictQueryPageReqVO reqVO);
}
