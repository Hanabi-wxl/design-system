package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.exception.GlobalException;
import edu.dlu.bysj.base.model.entity.OpenReport;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.OpenReportReviewVo;
import edu.dlu.bysj.base.model.vo.OpenReportVo;
import edu.dlu.bysj.base.model.vo.basic.BasicOpenReportVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.result.ResultCodeEnum;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.model.dto.TeacherReportComment;
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

    /*
     * @Description:
     * @Author: sinre 
     * @Date: 2022/7/2 17:22
     * @param subjectId
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.String>
     **/
    @GetMapping(value = "/openReport/teacherComment")
    @LogAnnotation(content = "查看开题报告教师评语")
    @RequiresPermissions({"openReport:teacherComment"})
    @ApiOperation(value = "查看开题报告教师评语")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<String> checkOpenReportComments(@NotNull Integer subjectId) {
        OpenReport opeReport = openReportService.getOne(new QueryWrapper<OpenReport>().eq("subject_id", subjectId));
        String message = "没有内容";
        if (ObjectUtil.isNotNull(opeReport)) {
            message = opeReport.getTeacherComment();
        }

        return CommonResult.success(message);
    }

    /*
     * @Description: 提交开题报告评语
     * @Author: sinre
     * @Date: 2022/6/27 13:13
     * @param comment
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/openReport/submitComment")
    @LogAnnotation(content = "提交开题报告评语(开题报告自查阶段)")
    @RequiresPermissions({"openReport:content"})
    @ApiOperation(value = "教师提交开题报告评语")
    public CommonResult<Object> teacherSubmitOpenReportComments(@RequestBody TeacherReportComment comment, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        String teacherComment = comment.getComment();
        String subjectId = comment.getSubjectId();
        String modifyOpinion = comment.getModifyOpinion();

        /*判断是否为该阶段*/
        Integer processCode = ProcessEnum.OPEN_REPORT_SELF_CHECK.getProcessCode();

        Subject subjectValue = subjectService.getById(subjectId);
        boolean openFlag = false, messageFlag = true;
        String message = "操作失败";
        OpenReport opeReport = openReportService.getOne(new QueryWrapper<OpenReport>().eq("subject_id", subjectId));
        /*当openReport, subject, 不为空并且处于(当前阶段和前一阶段)该阶段才能成功操作*/
        if (ObjectUtil.isNotNull(subjectValue) && ObjectUtil.isNotNull(opeReport)) {
            if (processCode.equals(subjectValue.getProgressId()) || processCode.equals(subjectValue.getProgressId() + 1)) {
                /*跟新openReport*/
                opeReport.setTeacherComment(teacherComment);
                opeReport.setTeacherDate(LocalDate.now());
                openFlag = openReportService.updateById(opeReport);
                /*跟新subject 所处的阶段*/
                subjectValue.setProgressId(processCode);
                subjectService.updateById(subjectValue);
            } else {
                return CommonResult.failed("该题目不在本阶段内");
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

    /*
     * @Description:
     * @Author: sinre
     * @Date: 2022/7/2 17:23
     * @param commonReviewVo
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @Transactional(rollbackFor = Exception.class)
    @PatchMapping(value = "/openReport/submitResult")
    @LogAnnotation(content = "提交开题报告审阅(专业级/院级)结果")
    @RequiresPermissions({"openReport:submitResult"})
    @ApiOperation(value = "提交开题报告审阅结果")
    public CommonResult<Object> submitOpenReportReview(@Valid @RequestBody OpenReportReviewVo commonReviewVo,
                                                       HttpServletRequest request) {

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
