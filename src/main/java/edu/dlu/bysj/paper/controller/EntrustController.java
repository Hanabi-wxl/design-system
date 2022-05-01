package edu.dlu.bysj.paper.controller;

import edu.dlu.bysj.base.model.entity.Entrust;
import edu.dlu.bysj.base.model.vo.EntrustInfoVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.EntrustService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/31 8:52
 */
@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "委托管理控制器")
@Validated
public class EntrustController {
    private final EntrustService entrustService;

    @Autowired
    public EntrustController(EntrustService entrustService) {
        this.entrustService = entrustService;
    }


    @GetMapping(value = "/entrust/setEntrust/{subjectId}/{consigneeId}")
    @LogAnnotation(content = "设置题目委托")
    @RequiresPermissions({"entrust:subjectEntrust"})
    @ApiOperation(value = "设置题目委托")
    public CommonResult<Object> setUpSubjectEntrust(@PathVariable("subjectId")
                                                    @NotNull Integer subjectId,
                                                    @PathVariable("consigneeId")
                                                    @NotNull Integer consigneeId,
                                                    HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        boolean save = false;
        if (!StringUtils.isEmpty(jwt)) {
            /*委托人id*/
            Integer userId = JwtUtil.getUserId(jwt);
            Entrust entrust = new Entrust();
            entrust.setSubjectId(subjectId);
            entrust.setConsigneeId(consigneeId);
            entrust.setConsignorId(userId);
            entrust.setDate(LocalDateTime.now());
            save = entrustService.save(entrust);
        }
        return save ? CommonResult.success(null) : CommonResult.failed();
    }


    @GetMapping(value = "/entrust/myList/{pageSize}/{pageNumber}/{year}")
    @LogAnnotation(content = "获取我的委托列表")
    @RequiresPermissions({"entrust:list"})
    @ApiOperation(value = "获取我的委托列表")
    public CommonResult<TotalPackageVo<EntrustInfoVo>> selfSubjectEntrust(@PathVariable("pageSize")
                                                                          @Min(value = 1, message = "每页记录数必须不小于1") Integer pageSize,
                                                                          @PathVariable("pageNumber")
                                                                          @Min(value = 1, message = "当前页码不得小于1") Integer pageNumber,
                                                                          @PathVariable("year")
                                                                          @NotBlank String year,
                                                                          HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        TotalPackageVo<EntrustInfoVo> result = null;
        if (!StringUtils.isEmpty(jwt)) {
            result = entrustService.selfEntrusts(JwtUtil.getUserId(jwt), year, pageNumber, pageSize);
        }
        return CommonResult.success(result);
    }

}
