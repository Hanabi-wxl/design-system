package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.dlu.bysj.base.model.query.GroupMemberQuery;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.base.util.JwtUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.entity.Team;
import edu.dlu.bysj.base.model.vo.GroupScoreVo;
import edu.dlu.bysj.base.model.vo.ModifyScoreVo;
import edu.dlu.bysj.base.model.vo.ScoreSummaryVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.defense.service.TeamService;
import edu.dlu.bysj.grade.service.ScoreService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author XiangXinGang
 * @date 2021/11/12 22:13
 */
@RestController
@RequestMapping(value = "/gradeManage")
@Api(tags = "成绩汇总控制器")
@Validated
public class GradeSummaryController {
    private final ScoreService scoreService;

    private final TeamService teamService;

    public GradeSummaryController(ScoreService scoreService, TeamService teamService) {
        this.scoreService = scoreService;
        this.teamService = teamService;
    }

    @GetMapping(value = "/score/summary/rank")
    @LogAnnotation(content = "查看成绩汇总")
    @RequiresPermissions({"summary:rank"})
    @ApiOperation(value = "获取成绩汇总表")
    @ApiImplicitParams({@ApiImplicitParam(name = "majorId", value = "专业id"),
        @ApiImplicitParam(name = "grade", value = "年级")})
    public CommonResult<ScoreSummaryVo> obtainScoreSummaryTable(@NotNull Integer majorId, @NotNull Integer year) {
        // TODO 汇总成绩分段标准
        Integer grade = GradeUtils.getGrade(year);
        ScoreSummaryVo result = scoreService.summaryScorePercentage(majorId, grade);
        return CommonResult.success(result);
    }

    @Deprecated
    @GetMapping(value = "/score/summary/group")
    @LogAnnotation(content = "按答辩分组获取学生成绩")
    @RequiresPermissions({"summary:group"})
    @ApiOperation(value = "按答辩分组获取成绩")
    @ApiImplicitParam(name = "groupId", value = "答辩分组id")
    public CommonResult<List<GroupScoreVo>>
        obtainScoreByDefenseGroup(@NotNull Integer groupId) {
        List<GroupScoreVo> groupScoreVos = scoreService.scoreOfSubjectTeam(groupId);
        return CommonResult.success(groupScoreVos);
    }

    @GetMapping(value = "/score/summary/groupMember")
    @LogAnnotation(content = "按答辩分组获取组内成员")
    @RequiresPermissions({"summary:group"})
    @ApiOperation(value = "按答辩分组获取组内成员")
    @ApiImplicitParam(name = "groupId", value = "答辩分组id")
    public CommonResult<TotalPackageVo<Map<String, Object>>>
    obtainGroupMember(@NotNull GroupMemberQuery query) {
        TotalPackageVo<Map<String, Object>> mapTotalPackageVo = scoreService.obtainGroupMember(query);
        return CommonResult.success(mapTotalPackageVo);
    }

    @GetMapping(value = "/score/summary/majorGroup")
    @LogAnnotation(content = "按专业,年级获取答辩分组信息")
    @RequiresPermissions({"summary:majorGroup"})
    @ApiOperation(value = "按专业,年级获取答辩分组信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "majorId", value = "专业id"),
        @ApiImplicitParam(name = "grade", value = "年级")})
    public CommonResult<List<Map<String, Object>>> obtainDefenseTeamInfo(
        @NotNull Integer majorId, @NotNull Integer grade) {
        List<Map<String, Object>> result = new ArrayList<>();
        grade = GradeUtils.getGrade(grade);
        List<Team> list = teamService.list(new QueryWrapper<Team>().eq("grade", grade).eq("major_id", majorId));
        if (list != null && !list.isEmpty()) {
            for (Team element : list) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("groupId", element.getId());
                map.put("groupNumber", element.getTeamNumber());
                result.add(map);
            }
        }
        return CommonResult.success(result);
    }

    @PostMapping(value = "/score/summary/changeScore")
    @LogAnnotation(content = "修改/新增总评成绩")
    @RequiresPermissions({"summary:change"})
    @ApiOperation(value = "修改总评成绩")
    public CommonResult<Object> modifySummaryScore(@Valid @RequestBody ModifyScoreVo modifyScoreVo, HttpServletRequest request) {
        Score score = scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", modifyScoreVo.getSubjectId()));
        String jwt = request.getHeader("jwt");
        boolean flag = false;
        if (ObjectUtil.isNotNull(score)) {
            score.setTotalPersonId(JwtUtil.getUserId(jwt));
            score.setTotalDate(LocalDate.now());
            score.setTotalComment(modifyScoreVo.getComment());
            score.setTotalSelfSummary(modifyScoreVo.getSummary());
            score.setTotalProcess(modifyScoreVo.getProcess());
            score.setTotalQuality(modifyScoreVo.getQuality());
            score.setTotalCompleteQuality(modifyScoreVo.getCompleteQuality());
            score.setTotalAbility(modifyScoreVo.getAbility());
            score.setIsSecond(modifyScoreVo.getIsSecond());
            flag = scoreService.updateById(score);
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败请查看该题目是否进入该成绩阶段");
    }
}
