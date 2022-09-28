package edu.dlu.bysj.paper.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.query.YearQuery;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.paper.model.dto.SelectStudentDto;
import edu.dlu.bysj.paper.model.dto.SubjectTopicDto;
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

    /*
     * @Description: 获取学生选题列表
     * @Author: sinre
     * @Date: 2022/6/20 21:05
     * @param query
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.TopicsVo>>
     **/
    @GetMapping(value = "/topics/studentList")
    @LogAnnotation(content = "获取学生选题列表")
    @RequiresPermissions({"topics:studentList"})
    @ApiOperation(value = "获取学生选题列表")
    public CommonResult<TotalPackageVo<TopicsVo>> studentSelectSubjectList(@Valid TopicsListQuery query, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        TotalPackageVo<TopicsVo> result = null;
        if (!StringUtils.isEmpty(jwt)) {
            /*年份*/
            Integer grade = GradeUtils.getGrade();
            result = subjectService.studentSelectSubjectInfo(query, JwtUtil.getMajorId(jwt), grade);
        }
        return CommonResult.success(result);
    }

    /*
     * @Description: 学生提交选题结果
     * @Author: sinre 
     * @Date: 2022/6/20 23:22
     * @param topicDto
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @PatchMapping(value = "/topics/studentSubmit")
    @LogAnnotation(content = "学生提交选题结果")
    @RequiresPermissions({"topics:studentSubmit"})
    @ApiOperation(value = "学生提交选题结果")
    public CommonResult<Object> submitChoseTopic(@RequestBody SubjectTopicDto topicDto, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        String firstSubjectId = topicDto.getFirstSubjectId();
        String secondSubjectId = topicDto.getSecondSubjectId();
        boolean flag = false;
        /*选题阶段操作码*/
        Integer currentCode = ProcessEnum.TOPIC_COLLEGE_AUDIT.getProcessCode();

        if (!StringUtils.isEmpty(jwt)) {
            Integer studentId = JwtUtil.getUserId(jwt);
            /*判断当前题目是否在可进行操作的阶段*/
            List<Subject> subjects = subjectService.listSubjectByIds(firstSubjectId, secondSubjectId);
            for (Subject element : subjects) {
                /*阶段为选题阶段和院审核阶段都可执行该操作*/
                if (!currentCode.equals(element.getProgressId())) {
                    /*表示题目中至少有一个不在操作范围内*/
                    return CommonResult.failed("选中的题目中至少有一个未到达该阶段的题目");
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
                    return CommonResult.failed("你的修改次数以到达阈值,无法再次修改");
                }
            }
            flag = topicService.saveOrUpdate(newTopic);
        }

        return flag ? CommonResult.success("操作成功该题目") : CommonResult.failed("操作失败，请再次尝试");
    }

    /*
     * @Description: 学生获取已选题目列表
     * @Author: sinre
     * @Date: 2022/6/20 23:15
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.util.List<edu.dlu.bysj.base.model.vo.SelectedVo>>
     **/
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

    /*
     * @Description: 教师已确定题目列表
     * @Author: sinre
     * @Date: 2022/6/25 16:19
     * @param year
     * @param pageSize
     * @param pageNumber
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.DetermineStudentVo>>
     **/
    @GetMapping(value = "/topics/teacherSelectedList")
    @LogAnnotation(content = "教师已确定题目列表")
    @RequiresPermissions({"topic:teacherList"})
    @ApiOperation(value = "教师已确定的题目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数"),
            @ApiImplicitParam(name = "pageNumber", value = "当前页码")
    })
    public CommonResult<TotalPackageVo<DetermineStudentVo>> teacherDetermineTopic(
            @Valid YearQuery query,
            HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Integer grade = GradeUtils.getGrade(query.getYear());
        TotalPackageVo<DetermineStudentVo> result = new TotalPackageVo<>();
        if (!StringUtils.isEmpty(jwt)) {
            result = subjectService.studentEnsureSubjectByTeacherId(JwtUtil.getUserId(jwt), grade, query.getPageNumber(), query.getPageSize());
        }
        return CommonResult.success(result);
    }

    /*
     * @Description: 教师查看他自己报题被学生选择情况
     * @Author: sinre
     * @Date: 2022/6/25 16:18
     * @param pageSize
     * @param pageNumber
     * @param year
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.TotalVolunteerPackage<edu.dlu.bysj.base.model.vo.UnselectStudentVo>>>
     **/
    @GetMapping(value = "/topics/teacherUnselectedList")
    @LogAnnotation(content = "教师查看他自己报题被学生选择情况")
    @RequiresPermissions({"topic:teacherUnselects"})
    @ApiOperation(value = "教师查看他自己报题被学生选择情况")
    public CommonResult<TotalPackageVo<TotalVolunteerPackage<UnselectStudentVo>>> teacherSelectStudentList(
            @Valid YearQuery query,
            HttpServletRequest request) {
        TotalPackageVo<TotalVolunteerPackage<UnselectStudentVo>> result = null;
        String jwt = request.getHeader("jwt");
        Integer grade = GradeUtils.getGrade(query.getYear());
        if (!StringUtils.isEmpty(jwt) && TYPE.equals(JwtUtil.getUserType(jwt))) {
            result = topicService.unselectStudentList(JwtUtil.getUserId(jwt), grade, query.getPageNumber(), query.getPageSize());
        }
        return CommonResult.success(result);
    }


    /*
     * @Description:教师从学生报题志愿中选取学生
     * @Author: sinre
     * @Date: 2022/6/25 16:49
     * @param dto
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @Transactional(rollbackFor = Exception.class)
    @PatchMapping(value = "/topics/submit")
    @LogAnnotation(content = "教师从学生报题志愿中选取学生")
    @RequiresPermissions({"topic:submit"})
    @ApiOperation(value = "教师从学生报题志愿中选取学生")
    public CommonResult<Object> teacherSelectStudent(@RequestBody SelectStudentDto dto) {
        Integer studentId = dto.getStudentId();
        String subjectId = dto.getSubjectId();
        boolean flag = false;
        boolean subjectFlag = false;
        String errorMessage = "";
        /*该题目必须到达(学生选题)，即学生选完题后才能进行该阶段操作*/
        /*当前subjectId 要为空否则该题目被其他专业占有*/
        Subject choseSubject = subjectService.getById(subjectId);
        Integer processCode = ProcessEnum.CHOOSE_TOPIC.getProcessCode();
        if (choseSubject.getProgressId().equals(processCode) || choseSubject.getProgressId().equals(processCode-1)) {
            if (ObjectUtil.isNotNull(choseSubject) && ObjectUtil.isNotNull(choseSubject.getSubjectId())) {
                //修改subject中该题的student.id, 和该学生表中的subjectId
                Student studentValue = studentService.getById(studentId);
                Integer originSubId = studentValue.getSubjectId();
                Subject originSub = subjectService.getById(originSubId);
                if(ObjectUtil.isNotNull(originSub) && originSub.getStudentId().equals(studentId)) {
                    return CommonResult.failed("该学生已选中题目: " + originSub.getSubjectName());
                }
                choseSubject.setStudentId(studentId);
                choseSubject.setProgressId(processCode);
                studentValue.setSubjectId(Integer.parseInt(subjectId));
                choseSubject.setMajorId(studentValue.getMajorId());
                /*同时跟新题目中的学生id,和学生中的最终确定题目id*/
                subjectFlag = subjectService.updateById(choseSubject);
                flag = studentService.updateById(studentValue);
            }
        } else {
            return CommonResult.failed("该题目不在本阶段内");
        }
        return (flag && subjectFlag) ? CommonResult.success("选题成功") : CommonResult.failed(errorMessage);
    }
}

