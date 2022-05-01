package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.entity.TotalTemplate;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.TotalScoreTemplateVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.grade.service.ScoreService;
import edu.dlu.bysj.grade.service.TotalTemplateService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author XiangXinGang
 * @date 2021/11/13 14:26
 */
@RestController
@RequestMapping(value = "/gradeManage")
@Api(tags = "总评控制器")
@Validated
public class SummaryEvaluationController {

    private final SubjectService subjectService;

    private final ScoreService scoreService;

    private final TotalTemplateService totalTemplateService;

    public SummaryEvaluationController(SubjectService subjectService, ScoreService scoreService,
        TotalTemplateService totalTemplateService) {
        this.subjectService = subjectService;
        this.scoreService = scoreService;
        this.totalTemplateService = totalTemplateService;
    }

    @GetMapping(value = "/score/total/commentFromScore")
    @LogAnnotation(content = "根据分数获取总评评语")
    @RequiresPermissions({"total:commentFrom"})
    @ApiOperation(value = "根据分数总和获取总评自评语")
    public CommonResult<Map<String, String>> obtainTemplateOfSummaryEvaluation(@Valid TotalScoreTemplateVo templateVo,
        HttpServletRequest request) {

        String jwt = request.getHeader("jwt");
        Map<String, String> result = new HashMap<>(16);
        boolean scoreFlag = false;
        Subject subject = subjectService.getById(templateVo.getSubjectId());
        if (ObjectUtil.isNotNull(subject)) {
            Integer processCode = ProcessEnum.GENERAL_EVALUATION.getProcessCode();
            /*判断是否可以在操作(在当前阶段和前一阶段)*/
            if (processCode.equals(subject.getProgressId() + 1) || processCode.equals(subject.getProgressId())) {
                Score scoreOne =
                    scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", templateVo.getSubjectId()));
                /*判断该题目是否存在*/
                if (ObjectUtil.isNotNull(scoreOne)) {
                    /*查看该专业是否有特别配置*/
                    Integer totalScore = (templateVo.getSummary() + templateVo.getProcess() + templateVo.getQuality()
                        + templateVo.getCompleteQuality() + templateVo.getAbility());

                    /*该专业分数小于等于score通用使用该评语*/
                    List<TotalTemplate> totalTemplate = totalTemplateService
                        .list(new QueryWrapper<TotalTemplate>().eq("major_id", JwtUtil.getMajorId(jwt))
                            .le("total_score", totalScore).orderByDesc("total_score"));

                    /*集合不为空值时采用特定配置*/
                    if (totalTemplate != null && !totalTemplate.isEmpty()) {
                        result.put("comment", totalTemplate.get(0).getContent());
                    }
                    // TODO 通用配置;
                    scoreOne.setTotalSelfSummary(templateVo.getSummary());
                    scoreOne.setTotalProcess(templateVo.getProcess());
                    scoreOne.setTotalQuality(templateVo.getQuality());
                    scoreOne.setTotalCompleteQuality(templateVo.getCompleteQuality());
                    scoreOne.setTotalAbility(templateVo.getAbility());
                    /*注意该方法要求所参与的数全部不为null否则抛出空指针异常触发回滚*/
                    Integer sum = scoreService.sumScore(scoreOne);
                    scoreOne.setSumScore(sum);
                    scoreFlag = scoreService.updateById(scoreOne);
                }
            }
        }
        return scoreFlag ? CommonResult.success(result) : CommonResult.failed("分数提交失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/score/total/comment")
    @RequiresPermissions({"total:comment"})
    @LogAnnotation(content = "教师提交总评评价")
    @ApiOperation(value = "教师提交总评评价")
    @ApiImplicitParams({@ApiImplicitParam(name = "comment", value = "评语模板"),
        @ApiImplicitParam(name = "subjectId", value = "题目id")})
    public CommonResult<Object> submitSummary(@RequestParam("comment") @NotBlank String comment,
        @RequestParam("subjectId") @NotNull Integer subjectId, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");

        Integer processCode = ProcessEnum.GENERAL_EVALUATION.getProcessCode();

        Subject subject = subjectService.getById(subjectId);

        boolean scoreFlag = false, subjectFlag = false;
        if (ObjectUtil.isNotNull(subject)) {
            if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                Score scoreOne = scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", subjectId));
                scoreOne.setTotalComment(comment);
                scoreOne.setTotalPersonId(JwtUtil.getUserId(jwt));
                scoreOne.setTotalDate(LocalDate.now());
                scoreFlag = scoreService.updateById(scoreOne);
                subject.setProgressId(processCode);
                subjectFlag = subjectService.updateById(subject);
            }
        }
        return (scoreFlag && subjectFlag) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @PostMapping(value = "/score/total/commentTemplate")
    @RequiresPermissions({"total:commentTemplate"})
    @LogAnnotation(content = "教师填写总评模板")
    @ApiOperation(value = "填写总评评语模板")
    @ApiImplicitParams({@ApiImplicitParam(name = "majorId", value = "专业id"),
        @ApiImplicitParam(name = "lowerBound", value = "分数段下界"),
        @ApiImplicitParam(name = "commentTemplate", value = "总评模板")})
    public CommonResult<Object> fillInTotalTemplate(@RequestParam("majorId") @NotNull Integer majorId,
        @RequestParam("lowerBound") @NotNull Integer lowerBound,
        @RequestParam("commentTemplate") @NotBlank String commentTemplate) {
        TotalTemplate template = totalTemplateService
            .getOne(new QueryWrapper<TotalTemplate>().eq("major_id", majorId).eq("total_score", lowerBound));
        if (ObjectUtil.isNull(template)) {
            template = new TotalTemplate();
        }
        template.setContent(commentTemplate);
        template.setTotalScore(lowerBound);
        template.setMajorId(majorId);

        boolean flag = totalTemplateService.saveOrUpdate(template);
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }
}
