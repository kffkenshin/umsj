package cn.yessoft.umsj.moduler.base.service;

import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictDetailVO;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictQueryReqVO;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDetailDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据字典明细表 服务类
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
public interface IBaseDictDetailService extends IService<BaseDictDetailDO> {


    List<BaseDictDetailDO> listQuery(DictQueryReqVO reqVO);

    void deleteDetail(List<Long> ids);

    void create(DictDetailVO reqVO);

    BaseDictDetailDO getByDictIdAndDetailKey(Integer dictId, String detailKey);
}
