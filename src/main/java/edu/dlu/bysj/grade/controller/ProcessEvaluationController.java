package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.ScoreProcessVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.grade.service.ScoreService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author XiangXinGang
 * @date 2021/11/13 10:58
 */
@RestController
@RequestMapping(value = "/gradeManage")
@Api(tags = "成绩过程管理控制器")
public class ProcessEvaluationController {
    private final SubjectService subjectService;

    private final ScoreService scoreService;

    public ProcessEvaluationController(SubjectService subjectService, ScoreService scoreService) {
        this.subjectService = subjectService;
        this.scoreService = scoreService;
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/score/process/commentFromScore")
    @LogAnnotation(content = "教师提交过程评价")
    @RequiresPermissions({"procedure:commentFroms"})
    @ApiOperation(value = "提交过程评价")
    public CommonResult<Object> submitProcessEvaluation(@Valid @RequestBody ScoreProcessVo processVo) {
        Subject subject = subjectService.getById(processVo.getSubjectId());
        /*处于过程管理阶段和前一阶段即可操作*/
        boolean scoreFlag = false, subjectFlag = false;
        Integer processCode = ProcessEnum.PROCESS_EVALUATION.getProcessCode();
        if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
            Score scoreOne = scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", processVo.getSubjectId()));
            if (ObjectUtil.isNotNull(scoreOne)) {
                scoreOne.setProcessAttitude(processVo.getAttitude());
                scoreOne.setProcessDiscipline(processVo.getDiscipline());
                scoreOne.setProcessReport(processVo.getReport());
                scoreOne.setProcessComplete(processVo.getComplete());
//                scoreOne.setProcessSimilar(processVo.getSimilar());
                scoreOne.setProcessComment(processVo.getComment());
                scoreOne.setProcessDate(LocalDate.now());
                scoreFlag = scoreService.updateById(scoreOne);
                subject.setProgressId(processCode);
                subjectFlag = subjectService.updateById(subject);
            }
        }
        return (scoreFlag && subjectFlag) ? CommonResult.success("操作成功") : CommonResult.failed("请检查该题目是否处于该阶段");
    }

}
