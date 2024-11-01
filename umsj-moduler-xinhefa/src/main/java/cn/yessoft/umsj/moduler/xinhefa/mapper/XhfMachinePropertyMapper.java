package cn.yessoft.umsj.moduler.xinhefa.mapper;

import cn.yessoft.umsj.moduler.xinhefa.entity.XhfMachinePropertyDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;

/**
 * 机台产能参数 Mapper 接口
 *
 * @author ethan
 * @since 2024-10-17
 */
public interface XhfMachinePropertyMapper extends YesBaseMapper<XhfMachinePropertyDO> {

  default List<XhfMachinePropertyDO> getListByMachineId(Long id) {
    return selectList(
        new LambdaQueryWrapper<XhfMachinePropertyDO>()
            .eq(XhfMachinePropertyDO::getMachineId, id)
            .orderByAsc(XhfMachinePropertyDO::getSeq));
  }
  ;
}
