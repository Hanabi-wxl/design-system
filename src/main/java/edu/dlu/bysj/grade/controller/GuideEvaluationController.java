package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.ScoreInformationVo;
import edu.dlu.bysj.base.model.vo.basic.BasicScoreVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.grade.service.ScoreService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author XiangXinGang
 * @date 2021/11/10 10:58
 */
@RestController
@RequestMapping("/gradeManage")
@Api(tags = "指导教师评价控制器")
@Validated
public class GuideEvaluationController {

    private final ScoreService scoreService;

    private final SubjectService subjectService;

    public GuideEvaluationController(ScoreService scoreService, SubjectService subjectService) {
        this.scoreService = scoreService;
        this.subjectService = subjectService;
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/score/firstTeacher/commentFromScore")
    @LogAnnotation(content = "指导教师填写评语,和打分")
    @RequiresPermissions({"mentor:commentFroms"})
    @ApiOperation(value = "提交指导教师评语和打分")
    public CommonResult<Object> submitGuideTeacherComments(@Valid @RequestBody BasicScoreVo basicScoreVo) {
        /*查询*/
        Subject subject = subjectService.getById(basicScoreVo.getSubjectId());
        Integer processCode = ProcessEnum.INSTRUCTOR_EVALUATION.getProcessCode();
        boolean scoreFlag = false;
        boolean subjectFlag = false;
        if (ObjectUtil.isNotNull(subject)) {
            /*位于论文提交后阶段(新增)和开题报告阶段(修改)可以操作*/
            if ((processCode.equals(subject.getProgressId() + 1) || processCode.equals(subject.getProgressId()))) {
                Score value =
                    scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", basicScoreVo.getSubjectId()));

                /*查询该题目是否有被评价，没有被评价,就采用插入策略，否则采用跟新策略*/
                if (ObjectUtil.isNull(value)) {
                    value = new Score();
                }
                value.setFirstQuality(basicScoreVo.getQuality());
                value.setFirstAbility(basicScoreVo.getAbility());
                value.setFirstComplete(basicScoreVo.getComplete());
                value.setSubjectId(basicScoreVo.getSubjectId());
                value.setFirstComment(basicScoreVo.getComment());
                value.setFirstDate(LocalDate.now());

                scoreFlag = scoreService.saveOrUpdate(value);

                /*跟新题目的所处的阶段*/
                subject.setProgressId(processCode);
                subjectFlag = subjectService.updateById(subject);
            }
        }
        return (scoreFlag && subjectFlag) ? CommonResult.success("操作成功") : CommonResult.failed("评语添加失败，请查看该题目所处的阶段");
    }

    @GetMapping(value = "/score/all")
    @RequiresPermissions({"score:all"})
    @LogAnnotation(content = "获取该题目的所有评分信息")
    @ApiOperation(value = "获取该题目的所有评分信息")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<ScoreInformationVo> obtainAllScoreInfo(@NotNull Integer subjectId) {
        Score score = scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", subjectId));
        ScoreInformationVo info = new ScoreInformationVo();

        if (ObjectUtil.isNotNull(score)) {
            info.setProcessAttitude(score.getProcessAttitude());
            info.setProcessDiscipline(score.getProcessDiscipline());
            info.setProcessReport(score.getProcessReport());
            info.setProcessComplete(score.getProcessComplete());
            info.setProcessComment(score.getProcessComment());
            info.setProcessSimilar(score.getProcessSimilar());
            info.setFirstQuality(score.getFirstQuality());
            info.setFirstAbility(score.getFirstAbility());
            info.setFirstComplete(score.getFirstComplete());
            info.setFirstComment(score.getFirstComment());
            info.setOtherQuality(score.getOtherQuality());
            info.setOtherAbility(score.getOtherAbility());
            info.setOtherComplete(score.getOtherComplete());
            info.setOtherComment(score.getOtherComment());
            info.setTotalSelfSummary(score.getTotalSelfSummary());
            info.setTotalProcess(score.getTotalProcess());
            info.setTotalQuality(score.getTotalQuality());
            info.setTotalCompleteQuality(score.getTotalCompleteQuality());
            info.setTotalAbility(score.getTotalAbility());
            info.setTotalComment(score.getTotalComment());
        }
        return CommonResult.success(info);

    }

}
