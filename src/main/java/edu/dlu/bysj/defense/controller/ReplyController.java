package edu.dlu.bysj.defense.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.*;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.ApplyReplyVo;
import edu.dlu.bysj.base.model.vo.ReplyInfo;
import edu.dlu.bysj.base.model.vo.ReplyTeacherVo;
import edu.dlu.bysj.base.model.vo.StudentGroupVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.defense.service.ProgressService;
import edu.dlu.bysj.defense.service.TeamConfigService;
import edu.dlu.bysj.defense.service.TeamService;
import edu.dlu.bysj.defense.service.TeamUserService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/7 20:11
 */
@RestController
@RequestMapping(value = "/defenseManagement")
@Api(tags = "答辩管理控制器")
@Validated
@Slf4j
public class ReplyController {

    private final TeamConfigService teamConfigService;

    private final TeamUserService teamUserService;

    private final StudentService studentService;

    private final TeamService teamService;

    private final SubjectService subjectService;

    private final ProgressService progressService;


    public ReplyController(TeamService teamService,
                           ProgressService progressService,
                           StudentService studentService,
                           TeamUserService teamUserService,
                           TeamConfigService teamConfigService,
                           SubjectService subjectService) {
        this.progressService = progressService;
        this.studentService = studentService;
        this.teamService = teamService;
        this.teamUserService = teamUserService;
        this.teamConfigService = teamConfigService;
        this.subjectService = subjectService;
    }

    @GetMapping(value = "/defence/teacherInfo")
    @LogAnnotation(content = "获取教师答辩信息")
    @RequiresPermissions({"defence:teacherInfo"})
    @ApiOperation(value = "获取教师答辩信息")
    @ApiImplicitParam(name = "grade", value = "年级")
    public CommonResult<List<ReplyTeacherVo>> obtainReplyTeacherInfo(HttpServletRequest request) {
        Integer grade = GradeUtils.getGrade();
        String jwt = request.getHeader("jwt");
        List<ReplyTeacherVo> result = null;
        if (!StringUtils.isEmpty(jwt)) {
            /*teamUser中 isStudent 0表示是否为学生*/
            result = teamService.teacherReplyInfo(JwtUtil.getUserId(jwt), grade, 0);
        }
        return CommonResult.success(result);
    }

    @GetMapping(value = "/defence/selfStudentInfo")
    @LogAnnotation(content = "查看与该教师相同答辩组同组的学生")
    @RequiresPermissions({"defence:selfInfo"})
    @ApiOperation(value = "查看该教师答辩组同组的学生答辩信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isSecond", value = "是否二答"),
            @ApiImplicitParam(name = "grade", value = "年级")
    })
    public CommonResult<List<StudentGroupVo>> studentInfoOfSimilarGuide(@NotNull Integer isSecond,
                                                                        HttpServletRequest request) {
        Integer grade = GradeUtils.getGrade();
        String jwt = request.getHeader("jwt");
        List<StudentGroupVo> result = null;
        if (!StringUtils.isEmpty(jwt)) {
            result = teamService.studentReplyInfoOfSimilarGroup(JwtUtil.getMajorId(jwt), grade, isSecond, JwtUtil.getUserId(jwt));
        }
        return CommonResult.success(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/defence/application")
    @LogAnnotation(content = "学生申请答辩")
    @RequiresPermissions({"defence:application"})
    @ApiOperation(value = "学生申请答辩")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<ApplyReplyVo> studentApplyDefense(HttpServletRequest request) {
        ApplyReplyVo result = new ApplyReplyVo();
        String jwt = request.getHeader("jwt");
        Integer userId = JwtUtil.getUserId(jwt);
        Integer processCode = ProcessEnum.APPLICATION_DEFENSE.getProcessCode();
        Integer subjectId = studentService.getById(userId).getSubjectId();
        boolean flag = false;
        List<String> need = null;

        Subject subject = subjectService.getById(subjectId);
        Progress progress = null;
        if (!StringUtils.isEmpty(jwt) && processCode.equals(subject.getProgressId() + 1)) {
            // TODO 验证 1-18 环节是否完成，从开始申报题目到申请答辩
//            need = teamUserService.undoneLink(subjectId, userId);
            /*当need中没有任何警告数据的时候执行申请答辩操作*/
            result.setNeed(need);

            if (need == null || need.isEmpty()) {
                /*答辩角色设置未4答辩人*/
                flag = teamUserService.addReplyStudent(JwtUtil.getUserId(jwt), JwtUtil.getMajorId(jwt), 4, subjectId);
                /*再次查询申请答辩后此人的组信息*/
                TeamUser teamuser = teamUserService.getOne(new QueryWrapper<TeamUser>()
                        .eq("user_id", userId)
                        .eq("is_student", 1)
                        .eq("resposiblity", 4));
                Team team = teamService.getById(teamuser.getTeamId());
                result.setGroupNumber(team.getTeamNumber());
                result.setSerial(teamuser.getSerial());
                result.setStartTime(team.getStartDate());
                result.setEndTime(team.getEndTime());
                result.setAddress(team.getAddress());
                result.setRoleName("答辩人");
                result.setRequirement(team.getRequest());

                /*跟新题目所处阶段*/
                subject.setProgressId(processCode);
                subjectService.updateById(subject);
            }
        } else if (subject.getProgressId() == 19){
            return CommonResult.failed("已申请答辩，请等待通知！");
        } else{
            progress = progressService.getOne(new QueryWrapper<Progress>().eq("id", subject.getProgressId() + 1));
        }
        return flag ? CommonResult.success(result) : CommonResult.failed(progress.getContent() + "阶段未完成！");
    }

    @Deprecated
    @GetMapping(value = "/defence/infoBySubjectId/{subjectId}")
    @LogAnnotation(content = "题目答辩信息")
    @RequiresPermissions({"defence:subjectInfo"})
    @ApiOperation(value = "根据题目id获取答辩信息")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<ReplyInfo> obtainDefenceInfo(@PathVariable("subjectId") @NotNull Integer subjectId,
                                                     HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        ReplyInfo replyInfo = new ReplyInfo();
        if (!StringUtils.isEmpty(jwt)) {
            TeamUser teamuser = teamUserService.getOne(new QueryWrapper<TeamUser>()
                    .eq("user_id", JwtUtil.getUserId(jwt))
                    .eq("is_student", 1)
                    .eq("resposiblity", 4));
            Team team = teamService.getById(teamuser.getTeamId());
            replyInfo.setGroupNumber(team.getTeamNumber());
            replyInfo.setSerial(teamuser.getSerial());
            replyInfo.setStartTime(team.getStartDate());
            replyInfo.setEndTime(team.getEndTime());
            replyInfo.setAddress(team.getAddress());
        }
        return CommonResult.success(replyInfo);
    }
}
