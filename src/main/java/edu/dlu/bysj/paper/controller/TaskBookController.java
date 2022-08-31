package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Message;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.entity.TaskBook;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.ContentVo;
import edu.dlu.bysj.base.model.vo.TaskBookDetailVo;
import edu.dlu.bysj.base.model.vo.TaskBookVo;
import edu.dlu.bysj.base.model.vo.basic.CommonReviewVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.MessageService;
import edu.dlu.bysj.paper.service.PlanService;
import edu.dlu.bysj.paper.service.TaskBookService;
import io.swagger.annotations.Api;
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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/2 22:02
 */
@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "任务书控制器")
@Validated
@Slf4j
public class TaskBookController {
    private final SubjectService subjectService;
    private final TaskBookService taskBookService;
    private final PlanService planService;
    private final MessageService messageService;

    public static final Integer MAJOR_REVIEW = 0;
    public static final Integer COLLEGE_REVIEW = 1;

    @Autowired
    public TaskBookController(SubjectService subjectService,
                              TaskBookService taskBookService,
                              PlanService planService,
                              MessageService messageService) {
        this.subjectService = subjectService;
        this.taskBookService = taskBookService;
        this.planService = planService;
        this.messageService = messageService;
    }

    /*
     * @Description: 教师提交任务书
     * @Author: sinre
     * @Date: 2022/6/25 21:02
     * @param taskBookVo
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/taskbook/teacherSubmit")
    @LogAnnotation(content = "教师提交任务书")
    @RequiresPermissions({"taskBook:teacherSubmit"})
    @ApiOperation(value = "教师提交任务书")
    public CommonResult<Object> teacherSubmitTaskBook(@RequestBody @Valid TaskBookVo taskBookVo) {

        long between = ChronoUnit.DAYS.between(taskBookVo.getStartTime(), taskBookVo.getEndTime());
        if (between < 0) {
            return CommonResult.validateFailed("开始结束的时间间隔必须大于1天");
        }
        /*判断是否处于该阶段*/
        Integer processCode = ProcessEnum.SUBMIT_TASK_BOOK.getProcessCode();
        Subject subjectValue = subjectService.getById(taskBookVo.getSubjectId());
        if (ObjectUtil.isNotNull(subjectValue)) {
            /*当前阶段和向前一阶段可以操作*/
            if (processCode.equals(subjectValue.getProgressId()) || processCode.equals(subjectValue.getProgressId() + 1)) {
                /*更新题目所处阶段*/
                subjectValue.setProgressId(processCode);
                /*插入任务书*/
                TaskBook taskBook = new TaskBook();
                taskBook.setSubjectId(taskBookVo.getSubjectId());
                taskBook.setAccording(taskBookVo.getAccording());
                taskBook.setDemand(taskBookVo.getDemand());
                taskBook.setEmphasis(taskBookVo.getEmphasis());
                taskBook.setReference(taskBookVo.getReference());
                taskBook.setStartDate(taskBookVo.getStartTime());
                taskBook.setEndDate(taskBookVo.getEndTime());
                taskBook.setMajorLeadingId(subjectValue.getMajorLeadingId());
                taskBook.setMajorDate(subjectValue.getMajorDate());
                taskBook.setCollegeLeadingId(subjectValue.getCollegeLeadingId());
                taskBook.setCollegeDate(subjectValue.getCollegeDate());
                taskBookService.saveOrUpdate(taskBook, new QueryWrapper<TaskBook>().eq("subject_id", taskBook.getSubjectId()));
                subjectService.updateById(subjectValue);
            } else {
                return CommonResult.failed("该题目不在本阶段内");
            }
        }
        return CommonResult.success("提交成功");
    }

    /*
     * @Description: 查看任务书详情
     * @Author: sinre
     * @Date: 2022/6/25 21:35
     * @param subjectId
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TaskBookDetailVo>
     **/
    @GetMapping(value = "/taskbook/detail")
    @LogAnnotation(content = "查看任务书详情")
    @RequiresPermissions({"taskBook:detail"})
    @ApiOperation(value = "查看任务书详情")
    public CommonResult<TaskBookDetailVo> checkTaskBookDetail(
            @Valid @NotNull(message = "课题信息不能为空") Integer subjectId) {
        /*查看该题目的taskBook 和 周计划信息*/
        Subject subject = subjectService.getById(subjectId);
        TaskBook taskbook = taskBookService.getOne(new QueryWrapper<TaskBook>().eq("subject_id", subjectId));
        TaskBookDetailVo taskBookDetailVo = new TaskBookDetailVo();

        /*查询该题目相关的content内容*/
        if (ObjectUtil.isNotNull(taskbook)) {
            List<ContentVo> contentVos = planService.checkPlans(subjectId);
            taskBookDetailVo.setContents(contentVos);
            taskBookDetailVo.setSubjectName(subject.getSubjectName());
            taskBookDetailVo.setStartTime(taskbook.getStartDate());
            taskBookDetailVo.setEndTime(taskbook.getEndDate());
            taskBookDetailVo.setAccording(taskbook.getAccording());
            taskBookDetailVo.setDemand(taskbook.getDemand());
            taskBookDetailVo.setEmphasis(taskbook.getEmphasis());
            taskBookDetailVo.setReference(taskbook.getReference());
            taskBookDetailVo.setContents(contentVos);
        }
        return CommonResult.success(taskBookDetailVo);
    }

    /*
     * @Description:
     * @Author: sinre 
     * @Date: 2022/6/27 18:30
     * @param agree
 * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/taskbook/submitResult")
    @LogAnnotation(content = "提交任务书审阅结果")
    @RequiresPermissions({"taskBook:submitResult"})
    @ApiOperation(value = "提交任务书审阅结果")
    public CommonResult<Object> submitReviewResult(@RequestBody @Valid CommonReviewVo agree, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        boolean taskFlag = false, subjectFlag = false;
        if (!StringUtils.isEmpty(jwt)) {
            Integer leadingId = JwtUtil.getUserId(jwt);
            Subject subject = subjectService.getById(agree.getSubjectId());
            TaskBook task = taskBookService.getOne(new QueryWrapper<TaskBook>().eq("subject_id", agree.getSubjectId()));
            Message message = new Message();
            message.setSenderId(leadingId);
            message.setReceiverId(subject.getStudentId());
            message.setSendTime(LocalDate.now());
            message.setContent(agree.getComment());
            if (MAJOR_REVIEW.equals(agree.getType())) {
                /*专业级审阅任务书,task,subject 同时不为空时跟新*/
                if (ObjectUtil.isNotNull(subject) && ObjectUtil.isNotNull(task)) {
                    Integer processCode = ProcessEnum.TASK_BOOK_MAJOR_AUDIT.getProcessCode();
                    /*当前阶段为修改,前一阶段为添加*/
                    if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                        /*跟新题目的状态和审阅结果*/
                        subject.setProgressId(processCode);
                        subjectFlag = subjectService.updateById(subject);
                        task.setMajorAgree(agree.getAgree());
                        task.setMajorDate(LocalDate.now());
                        task.setMajorLeadingId(leadingId);
                        taskFlag = taskBookService.updateById(task);
                        /*跟新成功后投递消息*/
                        message.setTitle("专业审核题目的意见");
                        if (!StringUtils.isEmpty(message.getContent())) {
                            messageService.save(message);
                        }
                    }
                }
            } else if (COLLEGE_REVIEW.equals(agree.getType())) {
                /*院级审核任务书*/
                if (ObjectUtil.isNotNull(subject) && ObjectUtil.isNotNull(task)) {
                    Integer processCode = ProcessEnum.TASK_BOOK_COLLEGE_AUDIT.getProcessCode();
                    /*当前阶段为修改，前一阶段为新增*/
                    if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                        subject.setProgressId(processCode);
                        subjectFlag = subjectService.updateById(subject);
                        task.setCollegeAgree(agree.getAgree());
                        task.setCollegeDate(LocalDate.now());
                        task.setCollegeLeadingId(leadingId);
                        taskFlag = taskBookService.updateById(task);
                        message.setTitle("院级审核题目意见");
                        /*投递消息*/
                        if (!StringUtils.isEmpty(message.getContent())) {
                            messageService.save(message);
                        }
                    }
                }
            }
        }
        return (taskFlag && subjectFlag) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败请检查该题目是否处于可操作的阶段");
    }

}
