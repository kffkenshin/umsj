package cn.yessoft.umsj.moduler.xinhefa.service;

import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfItemDO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Collection;
import java.util.List;

/**
 * 产品类型表 服务类
 *
 * @author ethan
 * @since 2024-09-19
 */
public interface IXhfItemService extends IService<XhfItemDO> {

  PageResult<XhfItemDO> pagedQuery(ItemQueryVO reqVO);

  List<XhfItemDO> listQuery(ItemQueryVO reqVO);

  void update(ItemVO reqVO);

  XhfItemDO validateExist(Long id);

  XhfItemDO getItemByNo(String itemNo);

  void insertOrUpdateBatch(Collection<XhfItemDO> entitys);

  List<XhfItemDO> getEmptyType2Items();

    List<XhfItemDO> getEmptyChannelItems();
}
