package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.dlu.bysj.base.model.query.MajorSearchQuery;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.grade.model.vo.CollegeCommentVo;
import edu.dlu.bysj.grade.model.vo.CommitGoodTeacherVo;
import edu.dlu.bysj.grade.model.vo.TeacherCommentVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.GoodTeacher;
import edu.dlu.bysj.base.model.vo.ExcellentTeacherTableVo;
import edu.dlu.bysj.base.model.vo.MajorExcellentTeacherVo;
import edu.dlu.bysj.base.model.vo.TeacherYearEvaluationVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.grade.service.GoodTeacherService;
import edu.dlu.bysj.grade.service.ScoreService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author XiangXinGang
 * @date 2021/11/14 10:25
 */
@RestController
@RequestMapping(value = "/gradeManage")
@Api(tags = "优秀教师控制器")
@Valid
public class ExcellentTeacherController {

    private final ScoreService scoreService;

    private final GoodTeacherService goodTeacherService;

    public ExcellentTeacherController(ScoreService scoreService, GoodTeacherService goodTeacherService) {
        this.scoreService = scoreService;
        this.goodTeacherService = goodTeacherService;
    }

    @GetMapping(value = "/score/goodTeacher/list")
    @LogAnnotation(content = "查看教师全部年份的评优情况")
    @RequiresPermissions({"goodTeacher:list"})
    @ApiOperation(value = "查看教师本人评优情况")
    public CommonResult<List<TeacherYearEvaluationVo>> checkTeacherExcellentCondition(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<TeacherYearEvaluationVo> result = scoreService.studentScoreNum(JwtUtil.getUserId(jwt));
        return CommonResult.success(result);
    }

    @GetMapping(value = "/score/goodTeacher/detail")
    @LogAnnotation(content = "查看优秀教师申报表")
    @RequiresPermissions({"goodeTeacher:detail"})
    @ApiOperation(value = "查看优秀教师申报表")
    @ApiImplicitParam(name = "year", value = "年份")
    public CommonResult<ExcellentTeacherTableVo>
        checkContentOfExcellentTeacher(@NotNull Integer year, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        ExcellentTeacherTableVo result = null;
        if (!StringUtils.isEmpty(jwt)) {
            result = scoreService.excellentInfoOfYear(JwtUtil.getUserId(jwt), year);
        }
        return CommonResult.success(result);
    }

    @PostMapping(value = "/score/goodTeacher/comment")
    @LogAnnotation(content = "提交指导教师评语")
    @RequiresPermissions({"goodTeacher:comment"})
    @ApiOperation(value = "提交指导教师评语")
    public CommonResult<Object> submitGoodTeacherComment(@RequestBody @Valid TeacherCommentVo teacherVo, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        boolean flag = false;
        if (!StringUtils.isEmpty(jwt)) {
            Integer userId = JwtUtil.getUserId(jwt);
            GoodTeacher goodTeacher = goodTeacherService
                .getOne(new QueryWrapper<GoodTeacher>().eq("teacher_id", userId).eq("school_year", teacherVo.getYear()));
            if (ObjectUtil.isNotNull(goodTeacher)) {
                goodTeacher.setTeacherId(userId);
                goodTeacher.setSchoolYear(teacherVo.getYear());
                goodTeacher.setSelfComment(teacherVo.getComment());
                flag = goodTeacherService.updateById(goodTeacher);
            }
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @GetMapping(value = "/score/goodTeacher/auditList")
    @LogAnnotation(content = "管理员获取审阅优秀教师列表")
    @RequiresPermissions({"goodTeacher:auditList"})
    @ApiOperation(value = "获取审阅优秀教师列表")
    public CommonResult<TotalPackageVo<MajorExcellentTeacherVo>> reviewGoodTeacherList(@Valid MajorSearchQuery query) {
        TotalPackageVo<MajorExcellentTeacherVo> packageVo = goodTeacherService.obtainGoodTeacherSelectionList(query);
        return CommonResult.success(packageVo);
    }

    @PostMapping(value = "/score/goodTeacher/commitGoodTeacher")
    @LogAnnotation(content = "管理员选择优秀教师")
    @RequiresPermissions({"goodTeacher:fix"})
    @ApiOperation(value = "确定优秀教师")
    public CommonResult<Object> sureGoodTeacher(@RequestBody @Valid CommitGoodTeacherVo teacher,HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        GoodTeacher goodTeacher = goodTeacherService
            .getOne(new QueryWrapper<GoodTeacher>().eq("teacher_id", teacher.getTeacherId()).eq("school_year", teacher.getYear()));

        if (ObjectUtil.isNull(goodTeacher)) {
            goodTeacher = new GoodTeacher();
            goodTeacher.setTeacherId(teacher.getTeacherId());
            goodTeacher.setSchoolYear(teacher.getYear());
        }
        if (teacher.getIsGood() == 1){
            goodTeacher.setCollegeAgree(1);
        } else {
            goodTeacher.setCollegeAgree(0);
        }
        goodTeacher.setCollegeDate(LocalDate.now());
        goodTeacher.setCollegeLeadingId(JwtUtil.getUserId(jwt));
        return goodTeacherService.saveOrUpdate(goodTeacher) ? CommonResult.success("操作成功")
                : CommonResult.failed("操作失败");
    }

    @PostMapping(value = "/score/goodTeacher/collegeComment")
    @LogAnnotation(content = "学院负责人提交优秀指导教师意见")
    @ApiOperation(value = "提交学院负责人关于优秀指导教师意见")
    @RequiresPermissions({"goodTeacher:collegeComment"})
    public CommonResult<Object> opinionOfGoodTeacher(@RequestBody @Valid CollegeCommentVo commentVo, HttpServletRequest request) {
        GoodTeacher goodTeacher = goodTeacherService
            .getOne(new QueryWrapper<GoodTeacher>()
                    .eq("teacher_id", commentVo.getTeacherId())
                    .eq("school_year", commentVo.getYear()));
        boolean flag = false;
        String jwt = request.getHeader("jwt");
        if (ObjectUtil.isNotNull(goodTeacher)) {
            goodTeacher.setCollegeAgree(commentVo.getAgree());
            goodTeacher.setCollegeComment(commentVo.getComment());
            goodTeacher.setCollegeDate(LocalDate.now());
            goodTeacher.setCollegeLeadingId(JwtUtil.getUserId(jwt));
            flag = goodTeacherService.updateById(goodTeacher);
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.success("操作失败");
    }
}
