package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.paper.model.dto.SelectAdjustDto;
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
import java.time.LocalDate;
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

    /*
     * @Description: 管理员获取未选题学生列表
     * @Author: sinre
     * @Date: 2022/6/25 16:43
     * @param query
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.util.List<edu.dlu.bysj.base.model.vo.UnselectStudentVo>>
     **/
    @GetMapping(value = "/topics/queryUnselectedStudentList")
    @LogAnnotation(content = "获取未选题学生列表")
    @RequiresPermissions({"topic:UnselectList"})
    @ApiOperation(value = "获取未选题学生列表")
    public CommonResult<List<UnselectStudentVo>> unChooseTopicStudent(UnselectStudentQuery query, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer year = query.getYear();
        if (ObjectUtil.isNull(year))
            year = LocalDate.now().getYear();
        Integer grade = GradeUtils.getGrade(year);
        List<UnselectStudentVo> unselectStudentVos =
                topicService.unChooseStudentList(query.getStudentNumber(), query.getStudentName(), grade, majorId);
        return CommonResult.success(unselectStudentVos);
    }

    /*
     * @Description:管理员获取未选题学生列表
     * @Author: sinre
     * @Date: 2022/6/25 17:04
     * @param query
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.util.List<edu.dlu.bysj.base.model.vo.UnSelectTopicVo>>
     **/
    @GetMapping(value = "topics/queryRestSubjectList")
    @LogAnnotation(content = "查询未被选择的题目")
    @RequiresPermissions({"topic:ResultList"})
    @ApiOperation(value = "未被选择题目列表")
    public CommonResult<List<UnSelectTopicVo>> remainingSubject(@Valid UnTopicListQuery query, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer year = query.getYear();
        if (ObjectUtil.isNull(year))
            year = LocalDate.now().getYear();
        Integer grade = GradeUtils.getGrade(year);
        List<UnSelectTopicVo> unSelectTopicVos = topicService.unChooseSubjectList(query.getTeacherNumber(), query.getTeacherName(), grade, majorId);
        return CommonResult.success(unSelectTopicVos);
    }

    /*
     * @Description: 专业管理员调配题目和学生
     * @Author: sinre
     * @Date: 2022/6/25 21:36
     * @param dto
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
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
                    subjectValue.setMajorId(value.getMajorId());
                    subjectValue.setProgressId(processCode);
                    studentFlag = studentService.updateById(value);
                    flag = subjectService.updateById(subjectValue);
                }
            } else {
                return CommonResult.failed("该题目不在本阶段内");
            }
        }
        return (flag && studentFlag) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败请检查题目和学生是否符合条件");
    }

    /*
     * @Description: 获取调配题目的指导教师题目信息列表
     * @Author: sinre
     * @Date: 2022/6/25 17:12
     * @param dto
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.StudentInfoVo>>
     **/
    @GetMapping(value = "/topics/majorAdjustList")
    @LogAnnotation(content = "专业管理员调配题目的指导教师题目信息列表")
    @RequiresPermissions({"topic:majorAdjusts"})
    @ApiOperation(value = "专业管理员调配题目的指导教师,题目信息列表")
    public CommonResult<TotalPackageVo<StudentInfoVo>> adjustFirstTeacherAboutSubject(@Valid SelectAdjustDto dto, HttpServletRequest request) {
        Integer majorId = dto.getMajorId();
        Integer year = GradeUtils.getGrade(dto.getYear());
        String searchContent = dto.getSearchContent();
        Integer pageNumber = dto.getPageNumber();
        Integer pageSize = dto.getPageSize();
        int type = 0;
        String userName = "";
        int userNumber = 0;
        if (searchContent.equals("")){
            type = 1;
        } else {
            try {
                userNumber = Integer.parseInt(searchContent);
            } catch (NumberFormatException e) {
                userName = searchContent;
            }
        }
        TotalPackageVo<StudentInfoVo> studentInfoVos;
        studentInfoVos = studentService.checkAdjustedSubjectMentor(pageNumber, pageSize, majorId, year, type, userName, userNumber);
        return CommonResult.success(studentInfoVos);
    }

    /*
     * @Description: 专业管理修改题目的第一指导教师
     * @Author: sinre
     * @Date: 2022/6/25 21:38
     * @param dto
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @PatchMapping(value = "/topics/majorSubmitAdjust")
    @LogAnnotation(content = "专业管理修改题目的第一指导教师")
    @RequiresPermissions({"topic:AdjustTeacher"})
    @ApiOperation(value = "专业管理员修改题目的第一指导教师")
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
