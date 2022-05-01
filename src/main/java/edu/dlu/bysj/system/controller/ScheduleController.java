package edu.dlu.bysj.system.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import edu.dlu.bysj.base.model.vo.FunctionTimeVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.result.ResultCodeEnum;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.system.service.FunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/15 20:33
 */
@RestController
@RequestMapping("/systemManagement")
@Api(tags = "时间表管理控制器")
public class ScheduleController {
    private final FunctionService functionService;


    @Autowired
    public ScheduleController(FunctionService functionService) {
        this.functionService = functionService;
    }

    @GetMapping(value = "/system/function/list/{majorId}")
    @LogAnnotation(content = "获取专业功能时间表")
    @ApiOperation(value = "获取专业功能时间表")
    @RequiresPermissions({"function:list"})
    @ApiImplicitParams({@ApiImplicitParam(name = "majorId", value = "专业id", required = true)})
    public CommonResult<List<FunctionTimeVo>> majorFunction(
            @PathVariable("majorId") Integer majorId) {
        List<FunctionTimeVo> functionTimeVos = functionService.allFunction(majorId);
        return CommonResult.success(functionTimeVos);
    }

    @GetMapping(value = "/system/function/change/{functionId}/{startTime}/{endTime}/{majorId}")
    @ApiOperation(value = "修改用户时间功能表")
    @LogAnnotation(content = "修改用户时间功能表")
    @RequiresPermissions({"function:change"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "functionId", value = "功能id", required = true),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true),
            @ApiImplicitParam(name = "majorId", value = "专业id", required = true)
    })
    public CommonResult<Object> changeFunctionTime(
            @PathVariable("functionId") Integer functionId,
            @PathVariable("startTime") LocalDateTime startTime,
            @PathVariable("endTime") LocalDateTime endTime,
            @PathVariable("majorId") Integer majorId) {
        Duration between = LocalDateTimeUtil.between(startTime, endTime);
        long l = between.toHours();
        // 如果时间小于1小时就返回操作失败
        if (l < 1) {
            return CommonResult.failed(ResultCodeEnum.VALIDATE_FAILED);
        }
        // 修改;
        boolean flag = functionService.updateFunctionTime(functionId, startTime, endTime);
        if (flag) {
            return CommonResult.success(ResultCodeEnum.SUCCESS);
        }

        return CommonResult.failed();
    }
}
