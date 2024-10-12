package cn.yessoft.umsj.moduler.xinhefa.controller;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.ItemQueryListVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.dto.XhfProductProcessRouteDTO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFWorkStationEnum;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfProductProcessRouteService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 工艺路线表 前端控制器
 * </p>
 *
 * @author ethan
 * @since 2024-09-21
 */
@RestController
@RequestMapping("/api/xhf/pp-route")
public class XhfProductProcessRouteController {
    @Resource
    private IXhfProductProcessRouteService xhfProductProcessRouteService;

    @PostMapping("/list-query")
    public ApiResult<List<XhfProductProcessRouteDTO>> queryList(@Valid @RequestBody ItemQueryListVO reqVO) {
        List<XhfProductProcessRouteDTO> r = xhfProductProcessRouteService.listQuery(reqVO);
        r.forEach(i -> {
            i.setWorkStationStr(XHFWorkStationEnum.valueOf(i.getWorkStation()) == null ? "" : XHFWorkStationEnum.valueOf(i.getWorkStation()).getName());
        });
        return success(r);
    }
}
