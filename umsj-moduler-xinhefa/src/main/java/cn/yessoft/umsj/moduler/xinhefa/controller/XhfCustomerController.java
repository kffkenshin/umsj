package cn.yessoft.umsj.moduler.xinhefa.controller;

import cn.yessoft.umsj.common.pojo.ApiResult;
import cn.yessoft.umsj.common.pojo.PageResult;
import cn.yessoft.umsj.common.utils.BeanUtils;
import cn.yessoft.umsj.moduler.base.service.IBaseAccountService;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerAuthVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerQueryReqVO;
import cn.yessoft.umsj.moduler.xinhefa.controller.vo.maindata.CustomerVO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerAuthDO;
import cn.yessoft.umsj.moduler.xinhefa.entity.XhfCustomerDO;
import cn.yessoft.umsj.moduler.xinhefa.enums.XHFCusMatchStrategyEnum;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerAuthService;
import cn.yessoft.umsj.moduler.xinhefa.service.IXhfCustomerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.yessoft.umsj.common.pojo.ApiResult.success;

/**
 * <p>
 * 客户信息表 前端控制器
 * </p>
 *
 * @author ethan
 * @since 2024-09-16
 */
@RestController
@RequestMapping("/api/xhf/customer")
public class XhfCustomerController {

    @Resource
    private IXhfCustomerAuthService xhfCustomerAuthService;

    @Resource
    private IBaseAccountService baseAccountService;

    @Resource
    private IXhfCustomerService xhfCustomerService;

    @PostMapping("/paged-query")
    public ApiResult<PageResult<CustomerVO>> query(@Valid @RequestBody CustomerQueryReqVO reqVO) {
        PageResult<XhfCustomerDO> pageResult = xhfCustomerService.pagedQuery(reqVO);
        return success(new PageResult<>(BeanUtils.toBean(pageResult.getList(), CustomerVO.class, customerVO -> {
            if (customerVO.getMatchStrategy() != null) {
                customerVO.setMatchStrategyStr(XHFCusMatchStrategyEnum.valueOf(customerVO.getMatchStrategy()).getName());
            }
        }), pageResult.getTotal()));
    }

    @PostMapping("/list-query")
    public ApiResult<List<CustomerVO>> queryList(@Valid @RequestBody CustomerQueryReqVO reqVO) {
        List<XhfCustomerDO> r = xhfCustomerService.listQuery(reqVO);
        return success(BeanUtils.toBean(r, CustomerVO.class));
    }

    @PostMapping("/auth/list-query")
    public ApiResult<List<CustomerAuthVO>> queryAuthList(@Valid @RequestBody CustomerQueryReqVO reqVO) {
        List<XhfCustomerAuthDO> r = xhfCustomerAuthService.queryAuthList(reqVO);
        return success(BeanUtils.toBean(r, CustomerAuthVO.class, customerVO -> {
            customerVO.setAccountName(baseAccountService.get(customerVO.getAccountId()).getNickName());
        }));
    }

    @PostMapping("/delete-auth")
    public ApiResult<String> deleteCustomerAuth(@RequestBody List<Long> ids) {
        xhfCustomerAuthService.delete(ids);
        return success();
    }


    @PostMapping("/update")
    public ApiResult<String> update(@Valid @RequestBody CustomerVO reqVO) {
        xhfCustomerService.update(reqVO);
        return success();
    }

    @GetMapping()
    public ApiResult<CustomerVO> get(@RequestParam("id") Long id) {
        XhfCustomerDO r = xhfCustomerService.validateExist(id);
        return success(BeanUtils.toBean(r, CustomerVO.class));
    }

    @PostMapping("/create-auth")
    public ApiResult<String> createAuth(@Valid @RequestBody CustomerAuthVO reqVO) {
        xhfCustomerAuthService.create(reqVO);
        return success();
    }

    @PostMapping("/update-auth")
    public ApiResult<String> updateAuth(@Valid @RequestBody CustomerAuthVO reqVO) {
        xhfCustomerAuthService.update(reqVO);
        return success();
    }

    @GetMapping("/customer-auth")
    public ApiResult<CustomerAuthVO> getMember(@RequestParam("id") Long id) {
        XhfCustomerAuthDO r = xhfCustomerAuthService.validateExist(id);
        return success(BeanUtils.toBean(r, CustomerAuthVO.class));
    }
}
