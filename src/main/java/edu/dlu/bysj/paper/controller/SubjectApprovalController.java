package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dlu.bysj.base.exception.GlobalException;
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
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.common.service.TeacherService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

    private final SubjectTypeService subjectTypeService;
    private final MajorService majorService;
    private final CollegeService collegeService;


    private static final Integer ONE = 1;
    private static final Integer ZERO = 0;

    @Autowired
    public SubjectApprovalController(TeacherService teacherService,
                                     SourceService sourceService,
                                     SubjectService subjectService,
                                     SubjectTypeService subjectTypeService,
                                     MajorService majorService,
                                     CollegeService collegeService) {
        this.teacherService = teacherService;
        this.sourceService = sourceService;
        this.subjectService = subjectService;
        this.subjectTypeService = subjectTypeService;
        this.majorService = majorService;
        this.collegeService = collegeService;
    }

    @GetMapping(value = "/approve/major-information")
    @LogAnnotation(content = "获取老师所属学院的所有专业")
    @RequiresPermissions({"approve:collegeMajor"})
    @ApiOperation(value = "获取老师所属学院的所有专业")
    public CommonResult<List<CollegeMajorVo>> teacherCollegeMajor(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<CollegeMajorVo> majorVos = null;
        if (!StringUtils.isEmpty(jwt)) {
            majorVos = teacherService.teacherMajorByCollegeId(JwtUtil.getMajorId(jwt));
        }
        return CommonResult.success(majorVos);
    }


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

    @GetMapping(value = "/approve/subjectOrigin")
    @LogAnnotation(content = "获取题目来源列表")
    @RequiresPermissions({"approve:subjectSource"})
    @ApiOperation(value = "获取题目来源列表")
    public CommonResult<List<SubjectSourceVo>> allSourceInfo() {
        List<SubjectSourceVo> result;
        result = sourceService.checkAllSourceInfo();
        return CommonResult.success(result);
    }


    // TODO: 2022/5/6 学生或教师报题
    @PostMapping(value = "/approve/teacherCommitSubject")
    @LogAnnotation(content = "教师提交/修改题目审批表")
    @RequiresPermissions({"approve:subjectApprove"})
    @ApiOperation(value = "教师提交/修改题目审批表")
    public CommonResult<Object> submitSubjectApproveTable(@NotNull @RequestBody SubjectApprovalVo subjectApprovalVo, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        boolean flag = false;
        /*新增*/
        if (subjectApprovalVo.getSubjectId().equals("")) {
            flag = subjectService.addedApprove(subjectApprovalVo, majorId);
        } else {
            /*修改*/
            flag = subjectService.modifyApprove(subjectApprovalVo);
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("报题超过限制!");
    }


    @GetMapping(value = "/approve/paperType")
    @LogAnnotation(content = "获取论文类型")
    @RequiresPermissions({"approve:subjectType"})
    @ApiOperation(value = "获取所有论文类型")
    public CommonResult<List<Map<String, Object>>> subjectTypeTable() {
        List<Map<String, Object>> maps = subjectTypeService.subjectTypeInfo();
        return CommonResult.success(maps);
    }


    @GetMapping(value = "/approve/singleTeacherInformation/{teacherId}")
    @LogAnnotation(content = "查询教师个人信息")
    @RequiresPermissions({"approve:teacherDetail"})
    @ApiOperation(value = "查询教师个人信息")
    public CommonResult<TeacherShortenVo> teacherShortenInformation(@PathVariable("teacherId") Integer teacherId) {
        TeacherShortenVo teacherShortenVos;
        try {
            teacherShortenVos = teacherService.teacherShortenInfo(teacherId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new GlobalException(500, "序列化异常，操作失败");
        }
        return CommonResult.success(teacherShortenVos);
    }

    @GetMapping(value = "/approve/detail")
    @LogAnnotation(content = "获取题目详情")
    @RequiresPermissions({"approve:subjectDetail"})
    @ApiOperation(value = "获取题目详情")
    public CommonResult<SubjectDetailInfoVo> subjectDetail(@RequestParam("subjectId") String subjectId) {
        SubjectDetailInfoVo subjectDetailInfoVo = subjectService.obtainsSubjectRelationInfo(subjectId);
        return CommonResult.success(subjectDetailInfoVo);

    }

    // TODO: 2022/5/6 查看题目列表
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

    // TODO: 2022/5/7 获取教师审批列表
    @GetMapping(value = "/approve/getListByTeacher")
    @LogAnnotation(content = "获取该教师的题目审批列表")
    @RequiresPermissions({"approve:subjectAudit"})
    @ApiOperation(value = "获取该教师的题目审批列表")
    public CommonResult<TotalPackageVo<ApproveDetailVo>> teacherApproveList(@Valid SubjectApproveListQuery query, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        query.setMajorId(majorId);
        query.setCollegeId(collegeService.getCollegeIdByMajorId(majorId));
        List<Integer> roleIds = JwtUtil.getRoleIds(jwt);
        Integer max = Collections.max(roleIds);
        TotalPackageVo<ApproveDetailVo> result = null;
        if (max == 3)
            result = subjectService.administratorApproveSubjectPagination(query);
        else if (max > 3)
            result = subjectService.collegeAdminiApproveSubjectPagination(query);
        return CommonResult.success(result);
    }

    @GetMapping(value = "/approve/userQueryList")
    @LogAnnotation(content = "根据条件查询(学号/教工号, 姓名)该管理员题目审核列表")
    @RequiresPermissions({"approve:auditEntrust"})
    @ApiOperation(value = "根据条件查询(学号/教工号, 姓名)该管理员审批列表")
    public CommonResult<TotalPackageVo<ApproveDetailVo>> teacherApproveListByCondition(@Valid ApproveConditionQuery query) {

        log.info("开始执行 type= {}", query.getType());
        TotalPackageVo<ApproveDetailVo> result = new TotalPackageVo<>();
        if (ZERO.equals(query.getType())) {
            /*学生*/
            result = subjectService.adminApprovePaginationByStudentCondition(query);
        } else if (ONE.equals(query.getType())) {
            /*教师*/
            log.info("开始执行 type= {}", query.getType());
            result = subjectService.adminApprovePaginationByTeacherCondition(query);
        }
        return CommonResult.success(result);
    }


    @GetMapping(value = "/approve/createFillingNumber/{majorId}/{year}")
    @LogAnnotation(content = "生成归档序号")
    @RequiresPermissions({"approve:FillingNumber"})
    @ApiOperation(value = "生成归档序号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "majorId", value = "专业id"),
            @ApiImplicitParam(name = "year", value = "年份")
    })
    public CommonResult<Object> generateFillingNumber(@PathVariable("majorId")
                                                      @NotNull Integer majorId,
                                                      @PathVariable("year")
                                                      @NotNull Integer year) {
        boolean flag;
        try {
            majorService.generateFillingNumber(majorId, year);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @GetMapping(value = "/approve/getMajorInformation/{collegeId}")
    @LogAnnotation(content = "根据学院id,获取学院下的专业名")
    @RequiresPermissions({"approve:majorInformation"})
    @ApiOperation(value = "根据学院id 获取学院下的专业")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "collegeId", value = "学院id")
    })
    public CommonResult<List<MajorSimpleInfoVo>> getAllCollegeMajor(@PathVariable("collegeId")
                                                                    @NotNull Integer collegeId) {
        List<MajorSimpleInfoVo> majorSimpleInfoVos = majorService.obtainCollegeMajor(collegeId);
        return CommonResult.success(majorSimpleInfoVos);
    }

    @GetMapping(value = "/approve/getCollegeInforamtion")
    @LogAnnotation(content = "获取该学校下的所学院信息")
    @RequiresPermissions({"approve:collegeInformation"})
    @ApiOperation(value = "获取该学校下的所有学院信息")
    public CommonResult<List<CollegeSimpleInoVo>> getCollegeInfo(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Integer schoolId = JwtUtil.getSchoolId(jwt);
        List<CollegeSimpleInoVo> result = null;
        if (!StringUtils.isEmpty(jwt)) {
            result = collegeService.obtainCollegeInfoBySchool(schoolId);
        }
        return CommonResult.success(result);
    }

    @PostMapping(value = "/approve/submitAuditResult")
    @LogAnnotation(content = "提专业/校级题目审阅结果")
    @RequiresPermissions({"approve:submitAudit"})
    @ApiOperation(value = "提交专业审阅/校级审阅题目结果")
    public CommonResult<Object> submitApproveResult(@Valid @RequestBody CommonReviewVo source, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");

        /*获取题目进展状况*/
        Subject value = subjectService.getBySubjectId(source.getSubjectId());
        boolean flag = false;
        /*专业级(当前阶段*/
        if (ZERO.equals(source.getType())) {
            Integer currentCode = ProcessEnum.TOPIC_MAJOR_AUDIT.getProcessCode();
            if (currentCode != null) {
                if (currentCode.equals(value.getProgressId()) || currentCode.equals(value.getProgressId() + 1)) {
                    value.setMajorAgree(source.getAgree());
                    if (source.getAgree() == 1)
                        value.setProgressId(currentCode);
                    else
                        value.setProgressId(1);
                    value.setMajorLeadingId(JwtUtil.getUserId(jwt));
                    value.setMajorDate(LocalDate.now());
                    flag = subjectService.updateById(value);
                }
            }
        } else if (ONE.equals(source.getType())) {
            /*学院级*/
            Integer currentCode = ProcessEnum.TOPIC_COLLEGE_AUDIT.getProcessCode();
            if (currentCode != null) {
                if (currentCode.equals(value.getProgressId()) || currentCode.equals(value.getProgressId() + 1)) {
                    value.setCollegeAgree(source.getAgree());
                    if (source.getAgree() == 1)
                        value.setProgressId(currentCode);
                    else
                        value.setProgressId(2);
                    value.setCollegeLeadingId(JwtUtil.getUserId(jwt));
                    value.setCollegeDate(LocalDate.now());
                    flag = subjectService.updateById(value);
                }
            }
        }
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @PostMapping(value = "/approve/submitAuditResults")
    @LogAnnotation(content = "批量提专业/校级题目审阅结果")
    @RequiresPermissions({"approve:submitAudit"})
    @ApiOperation(value = "批量提交专业审阅/校级审阅题目结果")
    public CommonResult<Object> submitApproveResults(@Valid @RequestBody CommonReviewsVo source, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<Integer> roleIds = JwtUtil.getRoleIds(jwt);
        assert roleIds != null;
        Integer max = Collections.max(roleIds);
        int type = max == 3 ? 0 : 1;
        /*获取题目进展状况*/
        List<Subject> subjects = subjectService.getBySubjectIds(source.getSubjectIds());
        boolean flag = true;
        for (Subject value : subjects) {
            /*专业级(当前阶段*/
            if (ZERO.equals(type)) {
                Integer currentCode = ProcessEnum.TOPIC_MAJOR_AUDIT.getProcessCode();
                if (currentCode != null) {
                    if (currentCode.equals(value.getProgressId()) || currentCode.equals(value.getProgressId() + 1)) {
                        value.setMajorAgree(1);
                        value.setProgressId(currentCode);
                        value.setMajorLeadingId(JwtUtil.getUserId(jwt));
                        value.setMajorDate(LocalDate.now());
                    }
                }
            } else if (ONE.equals(type)) {
                /*学院级*/
                Integer currentCode = ProcessEnum.TOPIC_COLLEGE_AUDIT.getProcessCode();
                if (currentCode != null) {
                    if (currentCode.equals(value.getProgressId()) || currentCode.equals(value.getProgressId() + 1)) {
                        value.setCollegeAgree(1);
                        value.setProgressId(currentCode);
                        value.setCollegeLeadingId(JwtUtil.getUserId(jwt));
                        value.setCollegeDate(LocalDate.now());
                    }
                }
            }
        }
        flag = subjectService.updateBatchById(subjects);
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @DeleteMapping(value = "/approve/deleteSubject")
    @LogAnnotation(content = "删除题目")
    @RequiresPermissions({"approve:deleteSubject"})
    @ApiOperation(value = "删除题目")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<Object> deleteSubject(@RequestBody String json) {
        String subjectId = JSONUtil.parseObj(json).get("id", String.class);
        return subjectService.removeSubjectById(subjectId) ? CommonResult.success(null) : CommonResult.failed();
    }
}
