package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.paper.model.dto.SelectStudentDto;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.query.UnTopicListQuery;
import edu.dlu.bysj.base.model.query.UnselectStudentQuery;
import edu.dlu.bysj.base.model.vo.StudentInfoVo;
import edu.dlu.bysj.base.model.vo.UnSelectTopicVo;
import edu.dlu.bysj.base.model.vo.UnselectStudentVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.model.dto.SubjectAdjustDto;
import edu.dlu.bysj.paper.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/1 16:19
 */
@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "选题审批控制器")
@Validated
@Slf4j
public class SelectApproveController {

    private final TopicService topicService;

    private final StudentService studentService;

    private final SubjectService subjectService;

    @Autowired
    public SelectApproveController(TopicService topicService,
                                   StudentService studentService,
                                   SubjectService subjectService) {
        this.topicService = topicService;
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    @GetMapping(value = "/topics/queryUnselectedStudentList")
    @LogAnnotation(content = "获取未选题学生列表")
    @RequiresPermissions({"topic:UnselectList"})
    @ApiOperation(value = "获取未选题学生列表")
    public CommonResult<List<UnselectStudentVo>> unChooseTopicStudent(@Valid UnselectStudentQuery query, HttpServletRequest request) {
        Integer majorId = query.getMajorId();
        if (ObjectUtil.isNull(majorId))
            majorId = JwtUtil.getMajorId(request.getHeader("jwt"));
        List<UnselectStudentVo> unselectStudentVos = topicService.unChooseStudentList(query.getStudentNumber(), query.getStudentName(), query.getYear()-3, majorId);
        return CommonResult.success(unselectStudentVos);
    }

    @GetMapping(value = "topics/queryRestSubjectList")
    @LogAnnotation(content = "查询未被选择的题目")
    @RequiresPermissions({"topic:ResultList"})
    @ApiOperation(value = "未被选择题目列表")
    public CommonResult<List<UnSelectTopicVo>> remainingSubject(@Valid UnTopicListQuery query, HttpServletRequest request) {
        Integer majorId = query.getMajorId();
        if (ObjectUtil.isNull(majorId))
            majorId = JwtUtil.getMajorId(request.getHeader("jwt"));
        List<UnSelectTopicVo> unSelectTopicVos = topicService.unChooseSubjectList(query.getTeacherNumber(), query.getTeacherName(), query.getYear(), majorId);
        return CommonResult.success(unSelectTopicVos);
    }

    @Transactional(rollbackFor = Exception.class)
    @PatchMapping(value = "/topics/majorSubmit")
    @LogAnnotation(content = "专业管理员调配题目和学生")
    @RequiresPermissions({"topic:majorSubmit"})
    @ApiOperation(value = "专业管理员调配剩余学生和剩余题目")
    public CommonResult<Object> deploymentSubject(@RequestBody SelectStudentDto dto) {
        /*相当于为学生选题*/
        String subjectId = dto.getSubjectId();
        Integer studentId = dto.getStudentId();
        boolean flag = false;
        boolean studentFlag = false;
        Subject subjectValue = subjectService.getById(subjectId);
        Student value = studentService.getById(studentId);
        if (ObjectUtil.isNotNull(subjectValue)) {
            Integer processCode = ProcessEnum.CHOOSE_TOPIC.getProcessCode();
            /*校验当期题目所处的阶段是是否为院级审核完成,或选题阶段*/
            if (processCode.equals(subjectValue.getProgressId()) || processCode.equals(subjectValue.getProgressId() + 1)) {
                /*当该题目没有被分配给学生时可以分配该题目, student.subjectId 默认初始化为-1, subject.studentId 默认初始化为Null*/
                if (ObjectUtil.isNotNull(value) && value.getSubjectId().equals(-1) && ObjectUtil.isNull(subjectValue.getStudentId())) {
                    value.setSubjectId(Integer.parseInt(subjectId));
                    subjectValue.setStudentId(studentId);
                    subjectValue.setProgressId(processCode);
                    studentFlag = studentService.updateById(value);
                    flag = subjectService.updateById(subjectValue);
                }
            }
        }
        return (flag && studentFlag) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败请检查题目和学生是否符合条件");
    }


    @GetMapping(value = "/topics/majorAdjustList")
    @LogAnnotation(content = "专业管理员调配题目的指导教师题目信息列表")
    @RequiresPermissions({"topic:majorAdjusts"})
    @ApiOperation(value = "专业管理员调配题目的指导教师,题目信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年级"),
            @ApiImplicitParam(name = "majorId", value = "专业"),
            @ApiImplicitParam(name = "type", value = "类型(1:学生, 0 教师)"),
            @ApiImplicitParam(name = "userName", value = "用户名"),
            @ApiImplicitParam(name = "userNumber", value = "学号/教工号")
    })
    public CommonResult<TotalPackageVo<StudentInfoVo>> adjustFirstTeacherAboutSubject(SubjectAdjustDto dto, HttpServletRequest request) {
        Integer majorId = dto.getMajorId();
        String type = dto.getType();
        Integer year = dto.getYear();
        String userName = dto.getUserName();
        String userNumber = dto.getUserNumber();
        Integer pageNumber = dto.getPageNumber();
        Integer pageSize = dto.getPageSize();
        majorId = ObjectUtil.isNull(majorId) ? JwtUtil.getMajorId(request.getHeader("jwt")) : majorId;
        TotalPackageVo<StudentInfoVo> studentInfoVos;
        /*sql 中 type 1:学生, 0 教师*/
        /*解决由于type String 为 "" 直接转换会出现异常*/
        if (NumberUtil.isNumber(type)) {
            studentInfoVos = studentService.checkAdjustedSubjectMentor(pageNumber, pageSize, majorId, year, Integer.valueOf(type), userName, userNumber);
        } else {
            /*当不为数字时，使用数字 3代码不进行模糊查询*/
            studentInfoVos = studentService.checkAdjustedSubjectMentor(pageNumber, pageSize, majorId, year, 3, userName, userNumber);
        }
        return CommonResult.success(studentInfoVos);
    }

    @PatchMapping(value = "/topics/majorSubmitAdjust")
    @LogAnnotation(content = "专业管理修改题目的第一指导教师")
    @RequiresPermissions({"topic:AdjustTeacher"})
    @ApiOperation(value = "专业管理员修改题目的第一指导教师")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId", value = "题目id"),
            @ApiImplicitParam(name = "firstTeacherId", value = "第一指导教师id")
    })
    public CommonResult<Object> modifySubjectMentor(@RequestBody SubjectAdjustDto dto) {
        String subjectId = dto.getSubjectId();
        Integer firstTeacherId = dto.getFirstTeacherId();
        Subject value = subjectService.getById(subjectId);
        boolean flag = false;
        if (ObjectUtil.isNotNull(value)) {
            value.setFirstTeacherId(firstTeacherId);
            flag = subjectService.updateById(value);
        }
        return flag ? CommonResult.success("修改成功") : CommonResult.failed("修改失败");
    }
}
