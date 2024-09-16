package cn.yessoft.umsj.moduler.base.mapper;

import cn.yessoft.umsj.moduler.base.entity.BaseFileConfigDO;
import cn.yessoft.umsj.mybatis.core.mapper.YesBaseMapper;

/**
 * <p>
 * 文件配置表 Mapper 接口
 * </p>
 *
 * @author ethan
 * @since 2024-09-07
 */
public interface BaseFileConfigMapper extends YesBaseMapper<BaseFileConfigDO> {
    default BaseFileConfigDO selectByMaster() {
        return selectOne(BaseFileConfigDO::getMaster, true);
    }

}
