package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.dlu.bysj.base.model.entity.EachMark;
import edu.dlu.bysj.base.model.query.SubjectListQuery;
import edu.dlu.bysj.base.model.vo.SubjectDetailVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.defense.service.EachMarkService;
import io.jsonwebtoken.Jwt;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    private final EachMarkService eachMarkService;

    public ScoreMutualController(SubjectService subjectService,EachMarkService eachMarkService, ScoreService scoreService) {
        this.subjectService = subjectService;
        this.eachMarkService = eachMarkService;
        this.scoreService = scoreService;
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/score/other/commentFromScore")
    @LogAnnotation(content = "提交互评评语")
    @RequiresPermissions({"each:commentFrom"})
    @ApiOperation(value = "提交互评")
    public CommonResult<Object> submitOtherEvaluation(@Valid @RequestBody BasicScoreVo scoreVo, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");

        Integer processCode = ProcessEnum.MUTUAL_EVALUATION.getProcessCode();
        List<EachMark> eachMarks = eachMarkService.list(new QueryWrapper<EachMark>()
                .eq("teacher_id", JwtUtil.getUserId(jwt)));
        Integer subjectId = scoreVo.getSubjectId();
        boolean flag = false;
        for (EachMark eachMark : eachMarks) {
            if (eachMark.getSubjectId().equals(subjectId)) {
                flag = true;
                break;
            }
        }
        if (!flag)
            return CommonResult.failed("互评异常");

        boolean subjectFlag = false, scoreFlag = false;
        Subject subject = subjectService.getById(scoreVo.getSubjectId());
        if (ObjectUtil.isNotNull(subject)) {
            if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                Score scoreOne =
                    scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", subjectId));
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
            } else {
                return CommonResult.failed("过程错误");
            }
        }
        return (scoreFlag && subjectFlag) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    /*
     * @Description:
     * @Author: sinre 
     * @Date: 2022/7/3 14:15
     * @param year
     * @param pageSize
     * @param pageNumber
     * @param request
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @GetMapping(value = "/score/other/list")
    @LogAnnotation(content = "获取互评列表")
    @RequiresPermissions({"each:list"})
    @ApiOperation(value = "获取互评")
    public CommonResult<Object> listOtherEvaluation(@Valid SubjectListQuery query, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        String userType = JwtUtil.getUserType(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        if (userType.equals("1")) {
            List<EachMark> eachMarks = eachMarkService.list(new QueryWrapper<EachMark>().eq("teacher_id", userId));
            List<Integer> idList = new LinkedList<>();
            for (EachMark eachMark : eachMarks) {
                idList.add(eachMark.getSubjectId());
            }
            Integer grade = GradeUtils.getGrade(query.getYear());
            TotalPackageVo<SubjectDetailVo> packageVo = new TotalPackageVo<>();
            if (idList.size() == 0)
                return CommonResult.success(packageVo);
            else
                packageVo = subjectService.filterByYear(idList, query.getPageSize(), query.getPageNumber(), grade);
            return CommonResult.success(packageVo);
        } else {
            TotalPackageVo<SubjectDetailVo> packageVo = subjectService.studentSubjectList(query, userId);
            return CommonResult.success(packageVo);
        }

    }
}
