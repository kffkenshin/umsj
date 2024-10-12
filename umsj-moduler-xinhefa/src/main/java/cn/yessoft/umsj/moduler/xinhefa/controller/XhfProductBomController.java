package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryListVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductBomDTO;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductBomService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BOM表 前端控制器
 *
 * @author ethan
 * @since 2024-09-21
 */
@RestController
@RequestMapping("/api/xhf/bom")
public class XhfProductBomController {
  @Resource private IXhfProductBomService xhfProductBomService;

  @PostMapping("/list-query")
  public ApiResult<List<XhfProductBomDTO>> queryList(@Valid @RequestBody ItemQueryListVO reqVO) {
    return success(xhfProductBomService.listQuery(reqVO));
  }
}
