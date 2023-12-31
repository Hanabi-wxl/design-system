package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dlu.bysj.base.exception.GlobalException;
import edu.dlu.bysj.base.model.dto.MajorFillingDto;
import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.query.ApproveConditionQuery;
import edu.dlu.bysj.base.model.query.SubjectApproveListQuery;
import edu.dlu.bysj.base.model.query.SubjectListQuery;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.base.model.vo.basic.CommonReviewVo;
import edu.dlu.bysj.base.model.vo.basic.CommonReviewsVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.common.service.TeacherService;
import edu.dlu.bysj.grade.service.ScoreService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.MessageService;
import edu.dlu.bysj.paper.service.SourceService;
import edu.dlu.bysj.paper.service.SubjectTypeService;
import edu.dlu.bysj.system.service.CollegeService;
import edu.dlu.bysj.system.service.MajorService;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/10/26 15:43
 */
@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "题目审批控制器")
@Validated
@Slf4j
public class SubjectApprovalController {
    private final TeacherService teacherService;
    private final SourceService sourceService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final SubjectTypeService subjectTypeService;
    private final MajorService majorService;
    private final CollegeService collegeService;
    private final ScoreService scoreService;
    private final MessageService messageService;

    private static final Integer ONE = 1;
    private static final Integer ZERO = 0;

    @Autowired
    public SubjectApprovalController(TeacherService teacherService,
                                     StudentService studentService,
                                     ScoreService scoreService,
                                     MessageService messageService,
                                     SourceService sourceService,
                                     SubjectService subjectService,
                                     SubjectTypeService subjectTypeService,
                                     MajorService majorService,
                                     CollegeService collegeService) {
        this.teacherService = teacherService;
        this.messageService = messageService;
        this.scoreService = scoreService;
        this.studentService = studentService;
        this.sourceService = sourceService;
        this.subjectService = subjectService;
        this.subjectTypeService = subjectTypeService;
        this.majorService = majorService;
        this.collegeService = collegeService;
    }

    /*
     * @Description: 获取本专业的所有老师
     * @Author: sinre
     * @Date: 2022/6/18 19:09
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.util.List<edu.dlu.bysj.base.model.vo.TeacherSimplyVo>>
     **/
    @GetMapping(value = "/approve/allMajorTeacher")
    @LogAnnotation(content = "获取本专业的所有老师")
    @RequiresPermissions({"approve:majorTeacher"})
    @ApiOperation(value = "获取本专业的所有老师")
    public CommonResult<List<TeacherSimplyVo>> majorAllTeacher(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<TeacherSimplyVo> result = null;
        if (!StringUtils.isEmpty(jwt)) {
            Integer majorId = JwtUtil.getMajorId(jwt);
            if (ObjectUtil.isNotNull(majorId)) {
                result = teacherService.majorTeacherSimplfyInfo(majorId);
            }
        }
        return CommonResult.success(result);
    }

    /*
     * @Description: 获取题目来源列表
     * @Author: sinre 
     * @Date: 2022/6/18 19:40
     * @param 
     * @return edu.dlu.bysj.base.result.CommonResult<java.util.List<edu.dlu.bysj.base.model.vo.SubjectSourceVo>>
     **/
    @GetMapping(value = "/approve/subjectOrigin")
    @LogAnnotation(content = "获取题目来源列表")
    @RequiresPermissions({"approve:subjectSource"})
    @ApiOperation(value = "获取题目来源列表")
    public CommonResult<List<SubjectSourceVo>> allSourceInfo() {
        List<SubjectSourceVo> result;
        result = sourceService.checkAllSourceInfo();
        return CommonResult.success(result);
    }

    /*
     * @Description: 教师提交/修改题目审批表
     * @Author: sinre 
     * @Date: 2022/6/18 20:09
     * @param subjectApprovalVo
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/approve/teacherCommitSubject")
    @LogAnnotation(content = "教师提交/修改题目审批表")
    @RequiresPermissions({"approve:subjectApprove"})
    @ApiOperation(value = "教师提交/修改题目审批表")
    public CommonResult<Object> submitSubjectApproveTable(@Valid @RequestBody SubjectApprovalVo subjectApprovalVo, HttpServletRequest request) {
        boolean flag = false;
        String jwt = request.getHeader("jwt");
        Integer userId = JwtUtil.getUserId(jwt);
        boolean isStudent = JwtUtil.getRoleIds(jwt).contains(1);
        Integer grade = GradeUtils.getGrade();
        StringBuilder subjectId = new StringBuilder();
        if (isStudent) {
            Integer number = studentService.idToNumber(userId);
            subjectId.append(grade+3).append(number).append("001");  // 学生报题编号
            if(!number.equals(subjectApprovalVo.getStudentNumber())) { // 学生判断是否填写他人学号
                return CommonResult.failed("请勿给他人报题");
            }
        } else {
            Integer teacherId = subjectApprovalVo.getTeacherId();
            String number = teacherService.idToNumber(teacherId);
            int sum = subjectService.count(new QueryWrapper<Subject>()
                    .eq("first_teacher_id", teacherId)
                    .eq("grade", grade));
            DecimalFormat decimalFormat = new DecimalFormat("000");
            String numFormat= decimalFormat.format(sum+1);
            subjectId.append(grade+3).append(number).append(numFormat); // 教师报题编号
            if(!teacherId.equals(userId)) { // 教师判断是否填写他人工号
                return CommonResult.failed("请勿给他人报题");
            }
        }
        /*新增*/
        if (subjectApprovalVo.getSubjectId().equals("")) {
            if(isStudent) { // 判断学生是否超过报题限制
                int sum = subjectService.count(new QueryWrapper<Subject>()
                        .eq("student_id", userId)
                        .eq("grade", grade));
                if(sum >= 1)
                    return CommonResult.failed("参与课题超过限制");
            }
            subjectApprovalVo.setSubjectId(subjectId.toString());
            // 提交报题
            flag = subjectService.addedApprove(subjectApprovalVo);
            Subject saveSubject = null;
            if (flag)
                saveSubject = subjectService.getBySubjectId(subjectId.toString());
            if(ObjectUtil.isNotNull(saveSubject) && isStudent) { // 如为学生将课题id填入学生信息
                Student student = studentService.getById(userId);
                student.setSubjectId(saveSubject.getId());
                studentService.updateById(student);
            }
            if(ObjectUtil.isNotNull(saveSubject)) { // 保存后填入成绩信息
                Score sc = new Score();
                sc.setSubjectId(saveSubject.getId());
                scoreService.save(sc);
            }
        } else { // 修改课题信息
            Integer progressId = subjectService.getOne(new QueryWrapper<Subject>()
                    .eq("subject_id", subjectApprovalVo.getSubjectId()))
                    .getProgressId();
            if (progressId > 2)
                return CommonResult.failed("学院审核后无法修改题目");
            else
                flag = subjectService.modifyApprove(subjectApprovalVo);
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    /*
     * @Description: 获取论文类型
     * @Author: sinre
     * @Date: 2022/6/18 19:37
     * @param
     * @return edu.dlu.bysj.base.result.CommonResult<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     **/
    @GetMapping(value = "/approve/paperType")
    @LogAnnotation(content = "获取论文类型")
    @RequiresPermissions({"approve:subjectType"})
    @ApiOperation(value = "获取所有论文类型")
    public CommonResult<List<Map<String, Object>>> subjectTypeTable() {
        List<Map<String, Object>> maps = subjectTypeService.subjectTypeInfo();
        return CommonResult.success(maps);
    }

    /*
     * @Description: 获取题目详情
     * @Author: sinre 
     * @Date: 2022/6/18 20:22
     * @param subjectId
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.SubjectDetailInfoVo>
     **/
    @GetMapping(value = "/approve/detail")
    @LogAnnotation(content = "获取题目详情")
    @RequiresPermissions({"approve:subjectDetail"})
    @ApiOperation(value = "获取题目详情")
    public CommonResult<SubjectDetailInfoVo> subjectDetail(
            @Valid @NotBlank(message = "题目信息不能为空") String subjectId) {
        SubjectDetailInfoVo subjectDetailInfoVo = subjectService.obtainsSubjectRelationInfo(subjectId);
        return CommonResult.success(subjectDetailInfoVo);

    }

    /*
     * @Description: 获取教师、学生课题
     * @Author: sinre
     * @Date: 2022/6/18 18:49
     * @param request
     * @param query
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.SubjectDetailVo>>
     **/
    @GetMapping(value = "/approve/subjectList")
    @LogAnnotation(content = "查看教师/学生自带的题目")
    @RequiresPermissions({"approve:subjectList"})
    @ApiOperation(value = "查看个人题目列表")
    public CommonResult<TotalPackageVo<SubjectDetailVo>> teacherSubjectList(HttpServletRequest request, @Valid SubjectListQuery query) {
        String jwt = request.getHeader("jwt");
        Integer roleId = Collections.max(JwtUtil.getRoleIds(jwt));
        Integer userId = JwtUtil.getUserId(jwt);
        TotalPackageVo<SubjectDetailVo> subjectVo;

        if (roleId > 1)
            subjectVo = subjectService.teacherSubjectList(query, userId);
        else
            subjectVo = subjectService.studentSubjectList(query,userId);
        return CommonResult.success(subjectVo);
    }

    /*
     * @Description: 管理员获取审批表
     * @Author: sinre
     * @Date: 2022/6/18 21:16
     * @param query
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.ApproveDetailVo>>
     **/
    @GetMapping(value = "/approve/getListByTeacher")
    @LogAnnotation(content = "获取该教师的题目审批列表")
    @RequiresPermissions({"approve:subjectAudit"})
    @ApiOperation(value = "获取该教师的题目审批列表")
    public CommonResult<TotalPackageVo<ApproveDetailVo>> teacherApproveList(
            @Valid SubjectApproveListQuery query, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<Integer> roleIds = JwtUtil.getRoleIds(jwt);
        Integer max = Collections.max(roleIds);
        query.setYear(GradeUtils.getGrade(query.getYear()));
        Integer queryMajorId = query.getMajorId();
        Integer queryCollegeId = query.getCollegeId();
        TotalPackageVo<ApproveDetailVo> result;
        if (max == 3){
            Integer majorId = JwtUtil.getMajorId(jwt);
            if (!majorId.equals(queryMajorId))
                return CommonResult.success(null);
        }
        if (max == 4){
            Integer collegeId = collegeService.getCollegeIdByMajorId(JwtUtil.getMajorId(jwt));
            if (!collegeId.equals(queryCollegeId))
                return CommonResult.success(null);
        }
        result = subjectService.adminApproveSubjectPagination(query);
        return CommonResult.success(result);
    }

    /*
     * @Description: 生成归档序号
     * @Author: sinre
     * @Date: 2022/6/19 18:25
     * @param majorFillingDto
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @PostMapping(value = "/approve/createFillingNumber")
    @LogAnnotation(content = "生成归档序号")
    @RequiresPermissions({"approve:FillingNumber"})
    @ApiOperation(value = "生成归档序号")
    public CommonResult<Object> generateFillingNumber(@RequestBody @Valid MajorFillingDto majorFillingDto) {
        boolean flag;
        try {
            majorService.generateFillingNumber(majorFillingDto.getMajorId(), majorFillingDto.getYear());
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    /*
     * @Description: 提专业/校级题目审阅结果
     * @Author: sinre 
     * @Date: 2022/6/18 22:07
     * @param source
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @PostMapping(value = "/approve/submitAuditResult")
    @LogAnnotation(content = "提专业/校级题目审阅结果")
    @RequiresPermissions({"approve:submitAudit"})
    @ApiOperation(value = "提交专业审阅/校级审阅题目结果")
    public CommonResult<Object> submitApproveResult(@Valid @RequestBody CommonReviewVo source, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");

        /*获取题目进展状况*/
        Subject subject = subjectService.getBySubjectId(source.getSubjectId());
        boolean flag = false;
        /*专业级(当前阶段*/
        if (ZERO.equals(source.getType())) {
            Integer currentCode = ProcessEnum.TOPIC_MAJOR_AUDIT.getProcessCode();
            if (currentCode != null) {
                if (currentCode.equals(subject.getProgressId()) || currentCode.equals(subject.getProgressId() + 1)) {
                    subject.setMajorAgree(source.getAgree());
                    if (source.getAgree() == 1)
                        subject.setProgressId(currentCode);
                    else
                        subject.setProgressId(1);
                    subject.setMajorLeadingId(JwtUtil.getUserId(jwt));
                    subject.setMajorDate(LocalDate.now());
                    flag = subjectService.updateById(subject);
                    messageService.sendMessage("专业审核题目意见", source.getComment(), JwtUtil.getUserId(jwt), subject.getFirstTeacherId(), 0);
                    if (subject.getStudentId() != null)
                        messageService.sendMessage("专业审核题目意见", source.getComment(), JwtUtil.getUserId(jwt), subject.getFirstTeacherId(), 0);
                } else {
                    return CommonResult.failed("该题目不在本阶段内");
                }
            }
        } else if (ONE.equals(source.getType())) {
            /*学院级*/
            Integer currentCode = ProcessEnum.TOPIC_COLLEGE_AUDIT.getProcessCode();
            if (currentCode != null) {
                if (currentCode.equals(subject.getProgressId()) || currentCode.equals(subject.getProgressId() + 1)) {
                    subject.setCollegeAgree(source.getAgree());
                    if (source.getAgree() == 1)
                        subject.setProgressId(currentCode);
                    else
                        subject.setProgressId(2);
                    subject.setCollegeLeadingId(JwtUtil.getUserId(jwt));
                    subject.setCollegeDate(LocalDate.now());
                    flag = subjectService.updateById(subject);
                    messageService.sendMessage("学院审核题目意见", source.getComment(), JwtUtil.getUserId(jwt), subject.getFirstTeacherId(), 0);
                    if (subject.getStudentId() != null)
                        messageService.sendMessage("学院审核题目意见", source.getComment(), JwtUtil.getUserId(jwt), subject.getFirstTeacherId(), 0);
                } else {
                    return CommonResult.failed("该题目不在本阶段内");
                }
            }
        }
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    /*
     * @Description: 批量提专业/校级题目审阅结果
     * @Author: sinre
     * @Date: 2022/6/19 18:13
     * @param source
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @PostMapping(value = "/approve/submitAuditResults")
    @LogAnnotation(content = "批量提专业/校级题目审阅结果")
    @RequiresPermissions({"approve:submitAudit"})
    @ApiOperation(value = "批量提交专业审阅/校级审阅题目结果")
    public CommonResult<Object> submitApproveResults(@Valid @RequestBody CommonReviewsVo source, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<Integer> roleIds = JwtUtil.getRoleIds(jwt);
        assert roleIds != null;
        /*获取题目进展状况*/
        String[] subjectIds = source.getSubjectIds();
        if(subjectIds.length == 0)
            return CommonResult.failed("题目为空");
        List<Subject> subjects = subjectService.getBySubjectIds(subjectIds);
        boolean flag = true;
        for (Subject value : subjects) {
            if (roleIds.contains(4)) {
                /*学院级*/
                Integer currentCode = ProcessEnum.TOPIC_COLLEGE_AUDIT.getProcessCode();
                if (currentCode != null) {
                    if (currentCode.equals(value.getProgressId()) || currentCode.equals(value.getProgressId() + 1)) {
                        value.setCollegeAgree(1);
                        value.setProgressId(currentCode);
                        value.setCollegeLeadingId(JwtUtil.getUserId(jwt));
                        value.setCollegeDate(LocalDate.now());
                    } else {
                        return CommonResult.failed("该题目不在本阶段内");
                    }
                }
            } else {
                Integer currentCode = ProcessEnum.TOPIC_MAJOR_AUDIT.getProcessCode();
                if (currentCode != null) {
                    if (currentCode.equals(value.getProgressId()) || currentCode.equals(value.getProgressId() + 1)) {
                        value.setMajorAgree(1);
                        value.setProgressId(currentCode);
                        value.setMajorLeadingId(JwtUtil.getUserId(jwt));
                        value.setMajorDate(LocalDate.now());
                    } else {
                        return CommonResult.failed("该题目不在本阶段内");
                    }
                }
            }
        }
        flag = subjectService.updateBatchById(subjects);
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    /*
     * @Description: 删除题目
     * @Author: sinre
     * @Date: 2022/6/19 18:16
     * @param json
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @DeleteMapping(value = "/approve/deleteSubject")
    @LogAnnotation(content = "删除题目")
    @RequiresPermissions({"approve:deleteSubject"})
    @ApiOperation(value = "删除题目")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<Object> deleteSubject(
            @Valid @NotBlank(message = "题目信息不能为空") @RequestBody String json) {
        String subjectId = JSONUtil.parseObj(json).get("id", String.class);
        return subjectService.removeSubjectById(subjectId) ? CommonResult.success(null) : CommonResult.failed();
    }
}
