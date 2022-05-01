package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.MiddleCheck;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.MiddleCheckDetailVo;
import edu.dlu.bysj.base.model.vo.ModifyMiddleCheckVo;
import edu.dlu.bysj.base.model.vo.basic.CommonReviewVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.MessageService;
import edu.dlu.bysj.paper.service.MiddleCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/11/3 22:44
 */

@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "中期检查控制器")
@Validated
@Slf4j
public class MiddleCheckController {

    private final MiddleCheckService middleCheckService;

    private final SubjectService subjectService;

    private final MessageService messageService;

    public static final Integer MAJOR_AUDIT = 0;

    public static final Integer COLLEGE_AUDIT = 1;

    @Autowired
    public MiddleCheckController(MiddleCheckService middleCheckService,
                                 SubjectService subjectService,
                                 MessageService messageService) {
        this.middleCheckService = middleCheckService;
        this.subjectService = subjectService;
        this.messageService = messageService;
    }


    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/middleCheck/content")
    @LogAnnotation(content = "提交修改中期检查表")
    @RequiresPermissions({"middleCheck:content"})
    @ApiOperation(value = "提交/修改中期检查表")
    public CommonResult<Object> modifyMiddleCheckTable(ModifyMiddleCheckVo checkVo) {

        Integer processCode = ProcessEnum.SUBMIT_MIDDLE_CHECK.getProcessCode();

        Subject subjectValue = subjectService.getById(checkVo.getSubjectId());

        String message = "操作失败请检查改题目是否到达指定阶段";

        if (ObjectUtil.isNotNull(subjectValue)) {
            /*当subject 所处的阶段位于(新增),开题报告院审查,或 提交开题报告阶段(修改)*/
            if (processCode.equals(subjectValue.getProgressId()) || processCode.equals(subjectValue.getProgressId() + 1)) {
                MiddleCheck middleCheckValue = middleCheckService.getOne(new QueryWrapper<MiddleCheck>().eq("subject_id", checkVo.getSubjectId()));
                /*当为提交中期检查表的时候middleCheckValue为空,创建新对象*/
                if (ObjectUtil.isNull(middleCheckValue)) {
                    middleCheckValue = new MiddleCheck();
                }
                middleCheckValue.setSubjectId(checkVo.getSubjectId());
                middleCheckValue.setDifficulty(checkVo.getDifficulty());
                middleCheckValue.setLiteratureQuantity(checkVo.getLiteratureQuantity());
                middleCheckValue.setWorkload(checkVo.getWorkLoad());
                middleCheckValue.setFinishDate(checkVo.getFinishDate());
                middleCheckValue.setHasTaskbook(checkVo.getHasTaskBook());
                middleCheckValue.setHasOpenreport(checkVo.getHasOpenReport());
                middleCheckValue.setAttitude(checkVo.getAttitude());
                middleCheckValue.setWorkingSpeed(checkVo.getWorkingProgress());
                middleCheckValue.setConclude(checkVo.getConclude());
                middleCheckValue.setArrange(checkVo.getArrange());
                middleCheckService.saveOrUpdate(middleCheckValue);
                /*修改subject所处的阶段*/
                subjectValue.setProgressId(processCode);
                subjectService.saveOrUpdate(subjectValue);
                message = "操作成功";
            }
        }
        return CommonResult.success(message);
    }


    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "/middleCheck/submitResult ")
    @LogAnnotation(content = "审核中期检查表")
    @RequiresPermissions({"middleCheck:submitResult"})
    @ApiOperation(value = "审核中期检查表")
    public CommonResult<Object> middleCheckAudit(@Valid CommonReviewVo reviewVo, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");

        Subject subjectValue = subjectService.getById(reviewVo.getSubjectId());
        MiddleCheck middleCheckValue = middleCheckService.getOne(new QueryWrapper<MiddleCheck>().eq("subject_id", reviewVo.getSubjectId()));

        boolean subjectFlag = false, middleCheckFlag = false;
        String message = "操作失败";

        if (!StringUtils.isEmpty(jwt)) {
            if (ObjectUtil.isNotNull(subjectValue) && ObjectUtil.isNotNull(middleCheckValue)) {
                if (MAJOR_AUDIT.equals(reviewVo.getType())) {
                    Integer processCode = ProcessEnum.MIDDLE_CHECK_MAJOR_AUDIT.getProcessCode();
                    if (processCode.equals(subjectValue.getProgressId()) || processCode.equals(subjectValue.getProgressId() + 1)) {
                        /*修改题目审批表和中期检查表*/
                        subjectValue.setProgressId(processCode);
                        middleCheckValue.setMajorAgree(reviewVo.getAgree());
                        middleCheckValue.setMajorDate(LocalDate.now());
                        middleCheckValue.setMajorLeadingId(JwtUtil.getUserId(jwt));
                        subjectFlag = subjectService.updateById(subjectValue);
                        middleCheckFlag = middleCheckService.updateById(middleCheckValue);
                        /*发送消息*/
                        messageService.sendMessage("专业审核中期检查表意见", reviewVo.getComment(), JwtUtil.getUserId(jwt), subjectValue.getStudentId(), 0);
                    } else {
                        message = "该题目没有到达符合的阶段";
                    }
                    /*专业审核*/
                } else if (COLLEGE_AUDIT.equals(reviewVo.getType())) {
                    /*校级审核*/
                    Integer processCode = ProcessEnum.MIDDLE_CHECK_COLLEGE_AUDIT.getProcessCode();
                    if (processCode.equals(subjectValue.getProgressId()) || processCode.equals(subjectValue.getProgressId() + 1)) {
                        subjectValue.setProgressId(processCode);
                        /*修改题目审批表和中期检查表*/
                        subjectValue.setProgressId(processCode);
                        middleCheckValue.setCollegeAgree(reviewVo.getAgree());
                        middleCheckValue.setCollegeDate(LocalDate.now());
                        middleCheckValue.setCollegeLeadingId(JwtUtil.getUserId(jwt));
                        subjectFlag = subjectService.updateById(subjectValue);
                        middleCheckFlag = middleCheckService.updateById(middleCheckValue);
                        /*发送消息*/
                        messageService.sendMessage("专业审核中期检查表意见", reviewVo.getComment(), JwtUtil.getUserId(jwt), subjectValue.getStudentId(), 0);
                    } else {
                        message = "该题目没有到达符合的阶段";
                    }
                }
            } else {
                message = "题目和中期检查表不存在";
            }
        }
        if (subjectFlag && middleCheckFlag) {
            message = "操作成功";
        }
        return CommonResult.success(message);
    }


    @GetMapping(value = "/middleCheck/detail/{subjectId}")
    @LogAnnotation(content = "查看中期检查表详情")
    @RequiresPermissions({"middleCheck:detail"})
    @ApiOperation(value = "查看中期检查表详情")
    @ApiImplicitParam(name = "subjectId", value = "题目Id")
    public CommonResult<MiddleCheckDetailVo> middleCheckDetailInfo(@PathVariable("subjectId") Integer subjectId) {

        MiddleCheck middleCheckValue = middleCheckService.getOne(new QueryWrapper<MiddleCheck>().eq("subject_id", subjectId));
        MiddleCheckDetailVo result = new MiddleCheckDetailVo();
        if (ObjectUtil.isNotNull(middleCheckValue)) {
            result.setLiteratureQuantity(middleCheckValue.getLiteratureQuantity());
            result.setFinishDate(middleCheckValue.getFinishDate());
            result.setWorkLoad(middleCheckValue.getWorkload());
            result.setDifficulty(middleCheckValue.getDifficulty());
            result.setAttitude(middleCheckValue.getAttitude());
            result.setHasTaskbook(middleCheckValue.getHasTaskbook());
            result.setHasOpenreport(middleCheckValue.getHasOpenreport());
            result.setConclude(middleCheckValue.getConclude());
            result.setArrange(middleCheckValue.getArrange());
        }

        return CommonResult.success(result);
    }
}
