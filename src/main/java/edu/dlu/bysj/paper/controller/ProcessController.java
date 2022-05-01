package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Process;
import edu.dlu.bysj.base.model.vo.ProcessDetailVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.ProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/5 22:31
 */
@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "过程管理控制器")
@Validated
public class ProcessController {

    private final ProcessService processService;

    @Autowired
    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping(value = "/process/checkPoint/{subjectId}/{week}")
    @LogAnnotation(content = "设置过程检查点")
    @RequiresPermissions({"process:checkPoint"})
    @ApiOperation(value = "设置过程检查点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId", value = "题目id"),
            @ApiImplicitParam(name = "week", value = "周次")

    })
    public CommonResult<Object> setProcessPoint(@PathVariable("subjectId") @NotNull Integer subjectId,
                                                @PathVariable("week") @NotNull Integer week) {
        Process process = new Process();
        process.setSubjectId(subjectId);
        process.setWeek(week);
        return processService.save(process) ? CommonResult.success("设置成功") : CommonResult.failed("设置失败");
    }


    @PostMapping(value = "/process/content/student")
    @LogAnnotation(content = "学生填写过程管理内容")
    @RequiresPermissions({"process:content"})
    @ApiOperation(value = "学生填写过程管理内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId", value = "题目id"),
            @ApiImplicitParam(name = "studentComment", value = "填写内容"),
            @ApiImplicitParam(name = "week", value = "周次")
    })
    public CommonResult<Object> studentFillInProcess(@RequestParam("subjectId") @NotNull Integer subjectId,
                                                     @RequestParam("studentComment") @NotBlank String studentComment,
                                                     @RequestParam("week") @NotNull Integer week) {
        Process value = processService.getOne(new QueryWrapper<Process>().eq("subject_id", subjectId).eq("week", week));

        boolean flag = false;
        if (ObjectUtil.isNotNull(value)) {
            value.setStudentComment(studentComment);
            value.setStudentDate(LocalDate.now());
            flag = processService.updateById(value);
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed("提交失败");
    }


    @PostMapping(value = "/process/content/teacher")
    @RequiresPermissions({"process:teacherContent"})
    @LogAnnotation(content = "教师填写过程管理内容")
    @ApiOperation(value = "教师填写过程管理内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId", value = "题目id"),
            @ApiImplicitParam(name = "teacherComment", value = "填写内容"),
            @ApiImplicitParam(name = "week", value = "周次")
    })
    public CommonResult<Object> teacherFillInProcess(@RequestParam("subjectId") @NotNull Integer subjectId,
                                                     @RequestParam("teacherComment") @NotBlank String teacherComment,
                                                     @RequestParam("week") @NotNull Integer week) {

        Process value = processService.getOne(new QueryWrapper<Process>().eq("subject_id", subjectId).eq("week", week));
        boolean flag = false;
        if (ObjectUtil.isNotNull(value)) {
            value.setTeacherComment(teacherComment);
            value.setTeacherDate(LocalDate.now());
            flag = processService.updateById(value);
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed("提交失败");
    }


    @GetMapping("/process/detail/{subjectId}")
    @LogAnnotation(content = "查看过程管理详情")
    @RequiresPermissions({"plan:detail"})
    @ApiOperation(value = "查看过程管理详情")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<List<ProcessDetailVo>> processDetailInfo(@PathVariable("subjectId") @NotNull Integer subjectId) {
        List<Process> subjectValue = processService.list(new QueryWrapper<Process>().eq("subject_id", subjectId));
        List<ProcessDetailVo> result = null;
        result = processService.processDetail(subjectId);
        return CommonResult.success(result);
    }


    @GetMapping(value = "/process/finishStatus/{subjectId}")
    @LogAnnotation(content = "查看过程管理完成情况")
    @RequiresPermissions({"process:finishStatus"})
    @ApiOperation(value = "查看过程管理完成情况")
    public CommonResult<List<Map<String, Object>>> processWeekStatus(@PathVariable("subjectId") @NotNull Integer subjectId) {
        List<Map<String, Object>> maps = processService.processStatus(subjectId);
        return CommonResult.success(maps);
    }
}
