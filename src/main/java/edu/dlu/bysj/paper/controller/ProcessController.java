package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Process;
import edu.dlu.bysj.base.model.vo.ProcessDetailVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.model.dto.ProcessDto;
import edu.dlu.bysj.paper.model.dto.ProcessListDto;
import edu.dlu.bysj.paper.service.ProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedList;
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

    @PostMapping(value = "/process/checkPoint")
    @LogAnnotation(content = "设置过程检查点")
    @RequiresPermissions({"process:checkPoint"})
    @ApiOperation(value = "设置过程检查点")
    public CommonResult<Object> setProcessPoint(@RequestBody ProcessDto dto) {
        Integer subjectId = dto.getSubjectId();
        List<Integer> weeks = dto.getWeeks();
        List<Process> processList = new LinkedList<>();
        for (Integer week : weeks) {
            Process process = new Process();
            process.setSubjectId(subjectId);
            process.setWeek(week);
            processList.add(process);
        }
        processService.remove(new QueryWrapper<Process>().eq("subject_id", subjectId));
        return processService.saveBatch(processList) ? CommonResult.success("设置成功") : CommonResult.failed("设置失败");
    }

    @PatchMapping(value = "/process/content/submit")
    @RequiresPermissions({"process:submitContent"})
    @LogAnnotation(content = "填写过程管理内容")
    @ApiOperation(value = "填写过程管理内容")
    public CommonResult<Object> teacherFillInProcess(@RequestBody ProcessListDto dto, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<Integer> roleIds = JwtUtil.getRoleIds(jwt);
        boolean isStudent = roleIds.contains(1);
        String subjectId = dto.getSubjectId();
        List<Process> processList = dto.getProcessList();
        List<Process> processes = processService.list(new QueryWrapper<Process>().eq("subject_id", subjectId));
        if (isStudent)
            for (Process process : processes) {
                for (Process process1 : processList) {
                    if (process.getWeek().equals(process1.getWeek())){
                        process.setStudentContent(process1.getStudentContent());
                        process.setStudentDate(LocalDate.now());
                    }
                }
            }
        else
            for (Process process : processes) {
                for (Process process1 : processList) {
                    if (process.getWeek().equals(process1.getWeek())){
                        process.setTeacherContent(process1.getTeacherContent());
                        process.setTeacherDate(LocalDate.now());
                    }
                }
            }
        boolean flag = false;
        flag = processService.updateBatchById(processes);
        return flag ? CommonResult.success("提交成功") : CommonResult.failed("提交失败");
    }


    @GetMapping("/process/detail")
    @LogAnnotation(content = "查看过程管理详情")
    @RequiresPermissions({"plan:detail"})
    @ApiOperation(value = "查看过程管理详情")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<List<ProcessDetailVo>> processDetailInfo(@NotNull Integer subjectId) {
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
