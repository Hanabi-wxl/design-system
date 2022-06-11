package edu.dlu.bysj.defense.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Team;
import edu.dlu.bysj.base.model.entity.TeamConfig;
import edu.dlu.bysj.base.model.entity.TeamUser;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
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
import org.hibernate.validator.constraints.Range;
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
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @author XiangXinGang
 * @date 2021/11/7 9:42
 */
@RestController
@RequestMapping(value = "/defenseManagement")
@Api(tags = "分组管理控制器")
@Validated
@Slf4j
public class GroupController {

    private final TeamService teamService;

    private final TeamUserService teamUserService;

    private final StudentService studentService;

    private final TeamConfigService teamConfigService;

    @Autowired
    public GroupController(TeamService teamService,
                           TeamUserService teamUserService,
                           StudentService studentService,
                           TeamConfigService teamConfigService) {
        this.teamService = teamService;
        this.teamUserService = teamUserService;
        this.studentService = studentService;
        this.teamConfigService = teamConfigService;
    }

    @GetMapping(value = "/defence/group/list")
    @LogAnnotation(content = "教师查看专业答辩分组")
    @RequiresPermissions({"group:list"})
    @ApiOperation(value = "查看专业答辩分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "majorId", value = "专业id"),
            @ApiImplicitParam(name = "isSecond", value = "是否二答")
    })
    public CommonResult<List<TeamInfoVo>> majorDefenseTeam(@NotNull Integer majorId,
                                                           @NotNull Integer isSecond) {
        int year = LocalDateTime.now().getYear();
        Integer grade = GradeUtils.getGrade(year);
        List<Team> list = teamService.list(new QueryWrapper<Team>().eq("major_id", majorId)
                .eq("grade", grade).eq("is_repeat", isSecond));
        List<TeamInfoVo> result = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (Team element : list) {
                TeamInfoVo teamInfoVo = new TeamInfoVo();
                teamInfoVo.setStartDate(element.getStartDate());
                teamInfoVo.setAddress(element.getAddress());
                teamInfoVo.setRequire(element.getRequest());
                teamInfoVo.setEndTime(element.getEndTime());
                DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("MM月dd日 HH:mm");
                DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("HH:mm");
                String rangeTime = element.getStartDate().format(dateTimeFormatter1) + " - " + element.getEndTime().format(dateTimeFormatter2);
                teamInfoVo.setRangeTime(rangeTime);
                teamInfoVo.setTeamNumber(element.getTeamNumber());
                teamInfoVo.setGroupId(element.getId());
                result.add(teamInfoVo);
            }
        }
        return CommonResult.success(result);
    }


    @GetMapping(value = "/defence/group/otherMajorTeacher")
    @LogAnnotation(content = "获取本院其他专业老师")
    @RequiresPermissions({"group:otherTeacher"})
    @ApiOperation(value = "获取本院其他专业老师")
    public CommonResult<List<TeacherSimplyVo>> collegeOtherMajorTeacher(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        List<TeacherSimplyVo> result = null;
        if (!StringUtils.isEmpty(jwt)) {
            Integer majorId = JwtUtil.getMajorId(jwt);
            result = teamService.otherMajorTeacherInfo(majorId);
        }
        return CommonResult.success(result);
    }

    @PostMapping(value = "/defence/group/teacherGroup")
    @LogAnnotation(content = "进行教师分组")
    @RequiresPermissions({"group:teacherGroup"})
    @ApiOperation(value = "提交教师分组")
    public CommonResult<Object> submitTeacherGroup(@Valid @RequestBody TeacherGroupVo group) {

        TeamUser teamUser = teamUserService.getOne(new QueryWrapper<TeamUser>()
                .eq("user_id", group.getTeacherId())
                .eq("team_id", group.getGroupId())
                .eq("is_student", 0));
        Optional<TeamUser> teamUserValue = Optional.ofNullable(teamUser);
        boolean flag;
        if (teamUserValue.isPresent()) {
            /*跟新*/
            teamUser.setTeamId(group.getGroupId());
            teamUser.setUserId(group.getTeacherId());
            teamUser.setResposiblity(group.getResponsibility());
            flag = teamUserService.updateById(teamUser);
        } else {
            /*新建*/
            TeamUser value = new TeamUser();
            value.setTeamId(group.getGroupId());
            value.setUserId(group.getTeacherId());
            value.setResposiblity(group.getResponsibility());
            /*1学生, 0非学生*/
            value.setIsStudent(0);
            flag = teamUserService.save(value);
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed("提交失败，查看教师信息");
    }

    @PostMapping(value = "defence/group/modifyGroupInfo")
    @LogAnnotation(content = "新增/修改分组信息")
    @RequiresPermissions({"group:modify"})
    @ApiOperation(value = "新增/修改分组信息")
    public CommonResult<Object> modifyGroupInformation(@Valid @RequestBody ModifyGroupVo groupVo,
                                                       HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        boolean flag = false;
        if (!StringUtils.isEmpty(jwt)) {
            Team team = teamService.getById(groupVo.getId());
            Optional<Team> teamOptional = Optional.ofNullable(team);
            if (!teamOptional.isPresent()) {
                team = new Team();
            }
            if (ObjectUtil.isNotNull(groupVo.getId())) {
                team.setId(groupVo.getId());
                team.setTeamNumber(groupVo.getTeamNumber());
            }

            team.setStartDate(groupVo.getStartDate());
            team.setTeamNumber(groupVo.getTeamNumber());
            team.setEndTime(groupVo.getEndTime());
            team.setAddress(groupVo.getAddress());
            team.setRequest(groupVo.getRequire());
            team.setGrade(GradeUtils.getGrade());
            team.setIsRepeat(groupVo.getIsRepeat());
            team.setType(groupVo.getType());
            team.setMajorId(JwtUtil.getMajorId(jwt));

            if (ObjectUtil.isNotNull(groupVo.getId())) {
                /*更新*/
                flag = teamService.updateById(team);
            } else {
                /*保存*/
                flag = teamService.save(team);
            }
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @GetMapping(value = "/defence/group/studentGroupList")
    @LogAnnotation(content = "学生查看自己在该组的第几答辩人")
    @RequiresPermissions({"group:studentGroup"})
    @ApiOperation(value = "获取学生答辩分组情况")
    @ApiImplicitParam(name = "groupId", value = "分组id")
    public CommonResult<Map<String, Object>> obtainStudentSequenceOfGroup(@NotNull Integer groupId,
                                                                          HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Map<String, Object> result = new HashMap<>(16);
        if (!StringUtils.isEmpty(jwt)) {
            Student student = studentService.getById(JwtUtil.getUserId(jwt));
            TeamUser teamUser = teamUserService.getOne(new QueryWrapper<TeamUser>()
                    .eq("user_id", JwtUtil.getUserId(jwt))
                    .eq("team_id", groupId)
                    .eq("is_student", 1));
            if (ObjectUtil.isNotNull(teamUser)) {
                result.put("serial", teamUser.getSerial());
                result.put("studentName", student.getName());
                result.put("studentId", student.getId());
            }
        }
        return CommonResult.success(result);
    }


    @GetMapping(value = "/defence/group/modifyStudentGroup/{studentId}/{groupId}/{serial}")
    @LogAnnotation(content = "管理员调整组内序号")
    @RequiresPermissions({"group:revision"})
    @ApiOperation(value = "调整学生组内序号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentId", value = "学生id"),
            @ApiImplicitParam(name = "groupId", value = "答辩分组id"),
            @ApiImplicitParam(name = "serial", value = "组内序列号")
    })
    public CommonResult<Object> adjustmentStudentSequenceOfGroup(@PathVariable("studentId") @NotNull Integer studentId,
                                                                 @PathVariable("groupId") @NotNull Integer groupId,
                                                                 @PathVariable("serial") @NotNull Integer serial) {

        boolean flag = false;
        try {
            teamUserService.adjustTeamUserOfStudent(studentId, groupId, serial, 1);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(value = "/defence/group/delete")
    @LogAnnotation(content = "删除分组")
    @RequiresPermissions({"group:delete"})
    @ApiOperation(value = "删除分组")
    @ApiImplicitParam(name = "groupId", value = "分组id")
    public CommonResult<Object> deleteGroup(@RequestBody String groupIdJson) {
        String groupId = JSONUtil.parseObj(groupIdJson).get("groupId", String.class);
        /*删除分组并删除该分组下的组员*/
        /*由是否发送异常判断操作是否成功,发生异常事务回滚则该删除操作没有成功,remove()方法在没有记录时返回值为false*/
        teamService.removeById(groupId);
        teamUserService.remove(new QueryWrapper<TeamUser>().eq("team_id", groupId));
        return CommonResult.success("操作成功");
    }

    @GetMapping(value = "/defence/group/rule/{majorId}/{grade}/{type}")
    @LogAnnotation(content = "设置专业分组规则")
    @RequiresPermissions({"group:rule"})
    @ApiOperation(value = "设置该年度专业分组规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "majorId", value = "专业id"),
            @ApiImplicitParam(name = "grade", value = "年级"),
            @ApiImplicitParam(name = "type", value = "答辩分组规则")
    })

    public CommonResult<Object> setUpStudentGroupRule(@PathVariable("majorId") @NotNull Integer majorId,
                                                      @PathVariable("grade") @NotNull Integer grade,
                                                      @PathVariable("type") @Range(min = 0, max = 3, message = "类型必须在[0,3]之内且为整数") Integer type) {
        /*判断是否存在*/
        TeamConfig teamconfig = teamConfigService.getOne(new QueryWrapper<TeamConfig>()
                .eq("major_id", majorId)
                .eq("grade", grade));
        Optional<TeamConfig> configOptional = Optional.ofNullable(teamconfig);
        if (!configOptional.isPresent()) {
            teamconfig = new TeamConfig();
        }

        teamconfig.setMajorId(majorId);
        teamconfig.setGrade(grade);
        teamconfig.setType(type);
        return teamConfigService.saveOrUpdate(teamconfig) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @GetMapping(value = "/defence/group/teacherInfo")
    @RequiresPermissions({"group:teacherInfo"})
    @LogAnnotation(content = "查看分组信息")
    @ApiOperation(value = "查看分组信息")
    public CommonResult<ModifyGroupVo> checkTeacherGroupInfo(@NotNull Integer groupId,HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Team team = teamService.getById(groupId);
        ModifyGroupVo groupVo = new ModifyGroupVo();
        groupVo.setAddress(team.getAddress());
        groupVo.setTeamNumber(team.getTeamNumber());
        groupVo.setType(team.getType());
        groupVo.setEndTime(team.getEndTime());
        groupVo.setId(team.getId());
        groupVo.setRequire(team.getRequest());
        groupVo.setStartDate(team.getStartDate());
        return CommonResult.success(groupVo);
    }

    @GetMapping(value = "/defence/group/studentInfo")
    @RequiresPermissions({"group:studentInfo"})
    @LogAnnotation(content = "查看学生分组信息")
    @ApiOperation(value = "查看学生分组信息")
    public CommonResult<ReplyInformationVo> checkStudentGroupInfo(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        ReplyInformationVo replyInformationVo = null;
        if (!StringUtils.isEmpty(jwt)) {
            /*1表示在该组中为学生*/
            replyInformationVo = teamUserService.checkDefenseGroupOfStudent(JwtUtil.getUserId(jwt), 1);
        }
        return CommonResult.success(replyInformationVo);
    }
}
