package cn.yessoft.umsj.moduler.base.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictDetailVO;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictQueryPageReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictQueryReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.dict.DictVO;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDO;
import cn.yessoft.umsj.moduler.base.entity.BaseDictDetailDO;
import cn.yessoft.umsj.moduler.base.service.IBaseDictDetailService;
import cn.yessoft.umsj.moduler.base.service.IBaseDictService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

/**
 * <p>
 * 数据字典表 前端控制器
 * </p>
 *
 * @author ethan
 * @since 2024-09-22
 */
@RestController
@RequestMapping("/api/base/dict")
public class BaseDictController {

    @Resource
    private IBaseDictDetailService baseDictDetailService;

    @Resource
    private IBaseDictService baseDictService;

    @PostMapping("/paged-query")
    public ApiResult<PageResult<DictVO>> query(@Valid @RequestBody DictQueryPageReqVO reqVO) {
        PageResult<BaseDictDO> pageResult = baseDictService.pagedQuery(reqVO);
        return success(new PageResult<>(BeanUtils.toBean(pageResult.getList(), DictVO.class),
                pageResult.getTotal()));
    }

    @PostMapping("/detail/list-query")
    public ApiResult<List<DictDetailVO>> queryDetailList(@Valid @RequestBody DictQueryReqVO reqVO) {
        List<BaseDictDetailDO> r = baseDictDetailService.listQuery(reqVO);
        return success(BeanUtils.toBean(r, DictDetailVO.class));
    }

    @PostMapping("/detail/delete")
    public ApiResult<String> deleteDetail(@RequestBody List<Long> ids) {
        baseDictDetailService.deleteDetail(ids);
        return success();
    }

    @PostMapping("/detail/create")
    public ApiResult<String> createDetail(@Valid @RequestBody DictDetailVO reqVO) {
        baseDictDetailService.create(reqVO);
        return success();
    }
}
