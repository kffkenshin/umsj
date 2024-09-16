package cn.yessoft.umsj.moduler.base.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.controller.vo.account.AccountQueryReqVO;
import cn.yessoft.umsj.moduler.base.controller.vo.account.BaseAccountRespVO;
import cn.yessoft.umsj.moduler.base.entity.BaseAccountDO;
import cn.yessoft.umsj.moduler.base.service.IBaseAccountService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author ethan
 * @since 2024-09-04
 */
@RestController
@RequestMapping("/api/base/account")
public class BaseAccountController {
    @Resource
    private IBaseAccountService baseAccountService;

    @PostMapping("/paged-query")
    public ApiResult<PageResult<BaseAccountRespVO>> query(@Valid @RequestBody AccountQueryReqVO reqVO) {
        PageResult<BaseAccountDO> pageResult = baseAccountService.pagedQuery(reqVO);
        return success(new PageResult<>(BeanUtils.toBean(pageResult.getList(), BaseAccountRespVO.class),
                pageResult.getTotal()));
    }

    @PostMapping("/list-query")
    public ApiResult<List<BaseAccountRespVO>> queryList(@Valid @RequestBody AccountQueryReqVO reqVO) {
        List<BaseAccountDO> r = baseAccountService.listQuery(reqVO);
        return success(BeanUtils.toBean(r, BaseAccountRespVO.class));
    }

    @PostMapping("/create")
    public ApiResult<String> create(@Valid @RequestBody BaseAccountRespVO reqVO) {
        baseAccountService.create(reqVO);
        return success();
    }

    @PostMapping("/update")
    public ApiResult<String> update(@Valid @RequestBody BaseAccountRespVO reqVO) {
        baseAccountService.update(reqVO);
        return success();
    }

    @GetMapping()
    public ApiResult<BaseAccountRespVO> get(@RequestParam("id") Long id) {
        BaseAccountDO r = baseAccountService.get(id);
        return success(BeanUtils.toBean(r, BaseAccountRespVO.class));
    }

    @PostMapping("/delete")
    public ApiResult<String> delete(@RequestBody List<Long> ids) {
        baseAccountService.delete(ids);
        return success();
    }

    @PostMapping("/init-pwd")
    public ApiResult<String> initPassword(@RequestBody List<Long> ids) {
        baseAccountService.initPassword(ids);
        return success();
    }

}
