package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.exception.GlobalException;
import edu.dlu.bysj.base.model.entity.OpenReport;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.OpenReportReviewVo;
import edu.dlu.bysj.base.model.vo.OpenReportVo;
import edu.dlu.bysj.base.model.vo.basic.BasicOpenReportVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.result.ResultCodeEnum;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.MessageService;
import edu.dlu.bysj.paper.service.OpenReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/11/3 11:20
 */
@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "开题报告控制器")
@Validated
@Slf4j
public class OpenReportController {

    private final OpenReportService openReportService;

    private final SubjectService subjectService;

    private final MessageService messageService;

    public static Integer MAJOR_TYPE = 0;

    public static Integer COLLEGE_TYPE = 1;

    @Autowired
    public OpenReportController(OpenReportService openReportService,
                                SubjectService subjectService,
                                MessageService messageService) {
        this.openReportService = openReportService;
        this.subjectService = subjectService;
        this.messageService = messageService;
    }

    @GetMapping(value = "/openReport/teacherComment/{subjectId}")
    @LogAnnotation(content = "查看开题报告教师评语")
    @RequiresPermissions({"openReport:teacherComment"})
    @ApiOperation(value = "查看开题报告教师评语")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<String> checkOpenReportComments(@PathVariable("subjectId") @NotNull Integer subjectId) {
        OpenReport opeReport = openReportService.getOne(new QueryWrapper<OpenReport>().eq("subject_id", subjectId));
        String message = "没有内容";
        if (ObjectUtil.isNotNull(opeReport)) {
            message = opeReport.getTeacherComment();
        }

        return CommonResult.success(message);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/openReport/change")
    @LogAnnotation(content = "修改开题报告")
    @RequiresPermissions({"openReport:change"})
    @ApiOperation(value = "修改开题报告")
    public CommonResult<Object> modifyOpenReport(@Valid OpenReportVo report) {

        OpenReport opeReport = openReportService.getOne(new QueryWrapper<OpenReport>().eq("subject_id", report.getSubjectId()));
        if (ObjectUtil.isNotNull(opeReport)) {
            /*修改数据库中的内容*/
            opeReport.setAccording(report.getAccording());
            opeReport.setSearchContent(report.getContent());
            opeReport.setSchedule(report.getSchedule());
            opeReport.setReference(report.getReference());
            opeReport.setLiterature(report.getReview());
            openReportService.updateById(opeReport);
            /*无论是数据库或则生成文件有一方有问题就回滚*/

            //TODO  做法1. 删除原有文件，并按照内容生成新文件 做法2. 以后每次生成新文件
        }
        return CommonResult.success("操作成功");
    }

    @GetMapping(value = "openReport/content/{subjectId}")
    @LogAnnotation(content = "获取开题报告内容")
    @RequiresPermissions({"openReport:content"})
    @ApiOperation(value = "获取开题报告内容")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<BasicOpenReportVo> openReportContent(@PathVariable("subjectId")
                                                             @NotNull Integer subjectId) {
        OpenReport opeReport = openReportService.getOne(new QueryWrapper<OpenReport>().eq("subject_id", subjectId));

        BasicOpenReportVo basicOpenReportVo = new BasicOpenReportVo();
        basicOpenReportVo.setAccording(opeReport.getAccording());
        basicOpenReportVo.setContent(opeReport.getSearchContent());
        basicOpenReportVo.setSchedule(opeReport.getSchedule());
        basicOpenReportVo.setReview(opeReport.getLiterature());
        basicOpenReportVo.setReference(opeReport.getReference());
        return CommonResult.success(basicOpenReportVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/openReport/submitComment")
    @LogAnnotation(content = "提交开题报告评语(开题报告自查阶段)")
    @RequiresPermissions({"openReport:content"})
    @ApiOperation(value = "教师提交开题报告评语")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId", value = "题目名称"),
            @ApiImplicitParam(name = "teacherComment", value = "教师评语"),
            @ApiImplicitParam(name = "modifyOpinion", value = "修改意见")
    })
    public CommonResult<Object> teacherSubmitOpenReportComments(@RequestParam(name = "subjectId") @NotNull Integer subjectId,
                                                                @RequestParam(name = "teacherComment") @NotBlank(message = "教师评语不能为空") String teacherComment,
                                                                @RequestParam(name = "modifyOpinion") String modifyOpinion,
                                                                HttpServletRequest request) {
        String jwt = request.getHeader("jwt");

        /*判断是否为该阶段*/
        Integer processCode = ProcessEnum.OPEN_REPORT_SELF_CHECK.getProcessCode();

        Subject subjectValue = subjectService.getById(subjectId);
        boolean openFlag = false, messageFlag = false;
        String message = "操作失败";
        OpenReport opeReport = openReportService.getOne(new QueryWrapper<OpenReport>().eq("subject_id", subjectId));
        /*当openReport, subject, 不为空并且处于(当前阶段和前一阶段)该阶段才能成功操作*/
        if (ObjectUtil.isNotNull(subjectValue) && ObjectUtil.isNotNull(opeReport)) {
            if (processCode.equals(subjectValue.getProgressId()) || processCode.equals(subjectValue.getProgressId() + 1)) {
                /*跟新openReport*/
                opeReport.setTeacherComment(teacherComment);
                openFlag = openReportService.updateById(opeReport);
                /*跟新subject 所处的阶段*/
                subjectValue.setProgressId(processCode);
                subjectService.updateById(subjectValue);
            }
            /*当存在审阅意见时就采取发送消息*/
            if (!StringUtils.isEmpty(modifyOpinion)) {
                messageFlag = messageService.sendMessage("教师提交开题报告评语",
                        modifyOpinion,
                        JwtUtil.getUserId(jwt),
                        subjectValue.getStudentId(),
                        0);
            }
        } else {
            message = "该题目还没有提交开题报告";
        }
        return (messageFlag && openFlag) ? CommonResult.success("操作成功") : CommonResult.failed(message);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/openReport/submitResult")
    @LogAnnotation(content = "提交开题报告审阅(专业级/院级)结果")
    @RequiresPermissions({"openReport:submitResult"})
    @ApiOperation(value = "提交开题报告审阅结果")
    public CommonResult<Object> submitOpenReportReview(@Valid OpenReportReviewVo commonReviewVo, HttpServletRequest request) {

        String jwt = request.getHeader("jwt");


        Subject subject = subjectService.getById(commonReviewVo.getSubjectId());
        OpenReport opeReport = openReportService.getOne(new QueryWrapper<OpenReport>().eq("subject_id", commonReviewVo.getSubjectId()));

        String message = "操作成功";
        if (!StringUtils.isEmpty(jwt)) {
            if (MAJOR_TYPE.equals(commonReviewVo.getType())) {
                Integer processCode = ProcessEnum.OPEN_REPORT_MAJOR_AUDIT.getProcessCode();

                /*open report 和 subject都存在*/
                if (ObjectUtil.isNotNull(subject) && ObjectUtil.isNotNull(opeReport)) {
                    /*题目处于该阶段*/
                    if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                        /*跟新题目，跟新开题报告*/
                        subject.setProgressId(processCode);
                        subjectService.updateById(subject);
                        opeReport.setMajorAgree(commonReviewVo.getAgree());
                        opeReport.setMajorLeadingId(JwtUtil.getUserId(jwt));
                        opeReport.setMajorDate(LocalDate.now());
                        openReportService.updateById(opeReport);
                        /*发送消息*/
                        messageService.sendMessage("专业级审核开题报告", commonReviewVo.getComment(), JwtUtil.getUserId(jwt), subject.getStudentId(), 0);
                    } else {
                        message = "题目没有到达该阶段";
                    }
                } else {
                    message = "题目不存在，或则该题目还没有开题报告";
                }
            } else if (COLLEGE_TYPE.equals(commonReviewVo.getType())) {
                Integer processCode = ProcessEnum.OPEN_REPORT_COLLEGE_AUDIT.getProcessCode();

                /*open report 和 subject都存在*/
                if (ObjectUtil.isNotNull(subject) && ObjectUtil.isNotNull(opeReport)) {
                    /*题目处于该阶段或前一阶段*/
                    if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                        /*跟新题目，跟新开题报告*/
                        subject.setProgressId(processCode);
                        subjectService.updateById(subject);
                        opeReport.setCollegeAgree(commonReviewVo.getAgree());
                        opeReport.setCollegeLeadingId(JwtUtil.getUserId(jwt));
                        opeReport.setCollegeDate(LocalDate.now());

                        openReportService.updateById(opeReport);
                        /*发送消息*/
                        messageService.sendMessage("专业级审核开题报告", commonReviewVo.getComment(), JwtUtil.getUserId(jwt), subject.getStudentId(), 0);
                    }
                } else {
                    message = "题目不存在，或则该题目还没有开题报告";
                }
            } else {
                throw new GlobalException(ResultCodeEnum.FAILED.getCode(), "操作类型错误");
            }

        }
        return CommonResult.success(message);
    }


}
