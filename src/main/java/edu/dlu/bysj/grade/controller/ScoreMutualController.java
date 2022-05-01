package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.basic.BasicScoreVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.grade.service.ScoreService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author XiangXinGang
 * @date 2021/11/13 15:43
 */
@RestController
@RequestMapping(value = "/gradeManage")
@Api(tags = "互评管理器")
@Valid
public class ScoreMutualController {
    private final SubjectService subjectService;

    private final ScoreService scoreService;

    public ScoreMutualController(SubjectService subjectService, ScoreService scoreService) {
        this.subjectService = subjectService;
        this.scoreService = scoreService;
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/score/other/commentFromScore")
    @LogAnnotation(content = "提交互评评语")
    @RequiresPermissions({"each:commentFrom"})
    @ApiOperation(value = "提交互评")
    public CommonResult<Object> submitOtherEvaluation(@Valid BasicScoreVo scoreVo, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");

        Integer processCode = ProcessEnum.MUTUAL_EVALUATION.getProcessCode();
        boolean subjectFlag = false, scoreFlag = false;
        Subject subject = subjectService.getById(scoreVo.getSubjectId());
        if (ObjectUtil.isNotNull(subject)) {
            if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                Score scoreOne =
                    scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", scoreVo.getSubjectId()));
                if (ObjectUtil.isNotNull(scoreOne)) {
                    scoreOne.setOtherQuality(scoreVo.getQuality());
                    scoreOne.setOtherAbility(scoreVo.getAbility());
                    scoreOne.setOtherComplete(scoreVo.getComplete());
                    scoreOne.setOtherComment(scoreVo.getComment());
                    scoreOne.setOtherPersonId(JwtUtil.getUserId(jwt));
                    scoreOne.setOtherDate(LocalDate.now());
                    scoreFlag = scoreService.updateById(scoreOne);
                    /*保持processCode ，这样能够一直到操作*/
                    subject.setProgressId(processCode);
                    subjectFlag = subjectService.updateById(subject);
                }
            }
        }
        return (scoreFlag && subjectFlag) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }
}
