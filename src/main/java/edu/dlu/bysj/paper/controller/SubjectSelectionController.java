package edu.dlu.bysj.paper.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.entity.Topics;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.query.TopicsListQuery;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.TopicService;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/31 10:33
 */
@RestController
@RequestMapping(value = "paperManagement")
@Api(tags = "选题管理控制器")
@Slf4j
@Validated
public class SubjectSelectionController {

    private final SubjectService subjectService;

    private final TopicService topicService;

    private final StudentService studentService;

    public static final Integer MAX_REVISE = 3;

    public static final String TYPE = "1";

    @Autowired
    public SubjectSelectionController(SubjectService subjectService,
                                      TopicService topicService,
                                      StudentService studentService) {
        this.subjectService = subjectService;
        this.topicService = topicService;
        this.studentService = studentService;
    }

    @GetMapping(value = "/topics/studentList")
    @LogAnnotation(content = "获取学生选题列表")
    @RequiresPermissions({"topics:studentList"})
    @ApiOperation(value = "获取学生选题列表")
    public CommonResult<TotalPackageVo<TopicsVo>> studentSelectSubjectList(@Valid TopicsListQuery query, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        TotalPackageVo<TopicsVo> result = null;
        if (!StringUtils.isEmpty(jwt)) {
            /*年份*/
            String year = DateUtil.format(new Date(), "yyyy");
            Integer grade = Integer.valueOf(year);
            result = subjectService.studentSelectSubjectInfo(query, JwtUtil.getMajorId(jwt), grade);
        }
        return CommonResult.success(result);
    }


    @GetMapping(value = "/topics/studentSubmit/{firstSubjectId}/{secondSubjectId}")
    @LogAnnotation(content = "学生提交选题结果")
    @RequiresPermissions({"topics:studentSubmit"})
    @ApiOperation(value = "学生提交选题结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "firstSubjectId", value = "第一志愿题目id"),
            @ApiImplicitParam(name = "secondSubjectId", value = "第二志愿题目id")
    })
    public CommonResult<Object> submitChoseTopic(@PathVariable("firstSubjectId")
                                                 @NotNull String firstSubjectId,
                                                 @PathVariable("secondSubjectId")
                                                 @NotNull String secondSubjectId,
                                                 HttpServletRequest request) {
        String jwt = request.getHeader("jwt");

        boolean flag = false;
        /*选题阶段操作码*/
        Integer currentCode = ProcessEnum.CHOOSE_TOPIC.getProcessCode();

        if (!StringUtils.isEmpty(jwt)) {
            Integer studentId = JwtUtil.getUserId(jwt);
            /*判断当前题目是否在可进行操作的阶段*/
            List<Subject> subjects = subjectService.listBySubjectIds(firstSubjectId, secondSubjectId);
            for (Subject element : subjects) {
                /*阶段为选题阶段和院审核阶段都可执行该操作*/
                if (!currentCode.equals(element.getProgressId() + 1) && !currentCode.equals(element.getProgressId())) {
                    /*表示题目中至少有一个不在操作范围内*/
                    return CommonResult.success("选中的题目中至少有一个未到达该阶段的题目");
                }
            }
            /*全部都在操作阶段,判断是插入还是修改, 一个学生对于1个topic*/
            Topics newTopic = topicService.getOne(new QueryWrapper<Topics>()
                    .eq("student_id", studentId));
            // 插入;
            if (ObjectUtil.isNull(newTopic)) {
                newTopic = new Topics();
                newTopic.setStudentId(studentId);
                newTopic.setFirstChange(0);
                newTopic.setSecondChange(0);
                newTopic.setFirstSubjectId(firstSubjectId);
                newTopic.setSecondSubjectId(secondSubjectId);
                newTopic.setStatus(1);
            } else {
                /*-127, 128 比较数字大小,*/
                if (newTopic.getFirstChange() < MAX_REVISE && newTopic.getSecondChange() < MAX_REVISE) {
                    newTopic.setFirstSubjectId(firstSubjectId);
                    newTopic.setSecondSubjectId(secondSubjectId);
                    newTopic.setFirstChange(newTopic.getFirstChange() + 1);
                    newTopic.setSecondChange(newTopic.getSecondChange() + 1);
                } else {
                    return CommonResult.success("你的修改次数以到达阈值,无法再次修改");
                }
            }

            flag = topicService.saveOrUpdate(newTopic);
            if (flag) {
                /*操作成功则跟新题目的状态*/
                for (Subject element : subjects) {
                    element.setProgressId(currentCode);
                    subjectService.updateById(element);
                }
            }
        }


        return flag ? CommonResult.success("操作成功该题目") : CommonResult.failed("操作失败，请再次尝试");
    }

    @GetMapping(value = "/topics/selectedList")
    @LogAnnotation(content = "学生获取已选题目列表")
    @RequiresPermissions({"topic:selectedList"})
    @ApiOperation(value = "获取学生已选题目列表")
    public CommonResult<List<SelectedVo>> selectedTopic(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<SelectedVo> selectedVos = null;
        if (!StringUtils.isEmpty(jwt)) {
            selectedVos = topicService.studentSelectSubject(JwtUtil.getUserId(jwt));
        }
        return CommonResult.success(selectedVos);
    }

    @GetMapping(value = "/topics/teacherSelectedList/{year}/{pageSize}/{pageNumber}")
    @LogAnnotation(content = "教师以确定题目列表")
    @RequiresPermissions({"topic:teacherList"})
    @ApiOperation(value = "教师以确定的题目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数"),
            @ApiImplicitParam(name = "pageNumber", value = "当前页码")
    })
    public CommonResult<TotalPackageVo<DetermineStudentVo>> teacherDetermineTopic(@PathVariable("year")
                                                                                  @NotNull Integer year,
                                                                                  @PathVariable("pageSize")
                                                                                  @Min(value = 1, message = "每页记录数不得少于1") Integer pageSize,
                                                                                  @PathVariable("pageNumber")
                                                                                  @Min(value = 1, message = "每页记录数不得少于1") Integer pageNumber,
                                                                                  HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        TotalPackageVo<DetermineStudentVo> result = new TotalPackageVo<>();
        if (!StringUtils.isEmpty(jwt)) {
            result = subjectService.studentEnsureSubjectByTeacherId(JwtUtil.getUserId(jwt), year, pageNumber, pageSize);
        }
        return CommonResult.success(result);
    }


    @GetMapping(value = "/topics/teacherUnselectedList/{pageSize}/{pageNumber}/{year}")
    @LogAnnotation(content = "教师查看他自己报题被学生选择情况")
    @RequiresPermissions({"topic:teacherUnselects"})
    @ApiOperation(value = "教师查看他自己报题被学生选择情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页记录数"),
            @ApiImplicitParam(name = "pageNumber", value = "当前页码"),
            @ApiImplicitParam(name = "year", value = "年级")
    })
    public CommonResult<TotalPackageVo<TotalVolunteerPackage<UnselectStudentVo>>> teacherSelectStudentList(@PathVariable("pageSize")
                                                                                                           @Min(value = 1, message = "每页记录数不得少于1") Integer pageSize,
                                                                                                           @PathVariable("pageNumber")
                                                                                                           @Min(value = 1, message = "每页记录数不得少于1") Integer pageNumber,
                                                                                                           @PathVariable("year")
                                                                                                           @NotNull Integer year,
                                                                                                           HttpServletRequest request) {
        TotalPackageVo<TotalVolunteerPackage<UnselectStudentVo>> result = null;
        String jwt = request.getHeader("jwt");
        /* 当jwt合格并且为老师才能进行该操作*/
        if (!StringUtils.isEmpty(jwt) && TYPE.equals(JwtUtil.getUserType(jwt))) {
            result = topicService.unselectStudentList(JwtUtil.getUserId(jwt), year, pageNumber, pageSize);
        }
        return CommonResult.success(result);
    }


    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/topics/submit")
    @LogAnnotation(content = "教师从学生报题志愿中选取学生")
    @RequiresPermissions({"topic:submit"})
    @ApiOperation(value = "教师从学生报题志愿中选取学生")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId", value = "题目id"),
            @ApiImplicitParam(name = "studentId", value = "学生id")
    })
    public CommonResult<Object> teacherSelectStudent(@RequestParam("subjectId")
                                                     @NotNull Integer subjectId,
                                                     @RequestParam("studentId")
                                                     @NotNull Integer studentId) {
        boolean flag = false;
        boolean subjectFlag = false;
        String errorMessage = "";
        /*该题目必须到达4阶段(学生选题)，即学生选完题后才能进行该阶段操作*/
        /*当前subjectId 要为空否则该题目被其他专业占有*/
        Subject value = subjectService.getById(subjectId);
        if (ObjectUtil.isNotNull(value) && ObjectUtil.isNull(value.getSubjectId())) {
            //修改subject中该题的student.id, 和该学生表中的subjectId
            Student studentValue = studentService.getById(subjectId);
            value.setStudentId(studentId);
            studentValue.setSubjectId(studentId);
            /*同时跟新题目中的学生id,和学生中的最终确定题目id*/
            subjectFlag = subjectService.updateById(value);
            flag = studentService.updateById(studentValue);
        } else {
            errorMessage = "操作错误";
            errorMessage = "操作失败";
            if (ObjectUtil.isNull(value)) {
                errorMessage = "题目Id为空";
            } else {
                if (ObjectUtil.isNotNull(value.getStudentId())) {
                    errorMessage = "该题目已被您所报题目所属其他专业选择";
                }
            }
        }
        return (flag && subjectFlag) ? CommonResult.success("选题成功") : CommonResult.failed();
    }
}

