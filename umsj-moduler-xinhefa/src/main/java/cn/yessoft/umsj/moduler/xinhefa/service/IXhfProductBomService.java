package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryListVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfProductBomDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductBomDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * BOM表 服务类
 *
 * @author ethan
 * @since 2024-09-21
 */
public interface IXhfProductBomService extends IService<XhfProductBomDO> {

  List<XhfProductBomDTO> listQuery(ItemQueryListVO reqVO);

  void deleteByItemId(Long id);

  void insertBatch(List<XhfProductBomDO> results);
}
