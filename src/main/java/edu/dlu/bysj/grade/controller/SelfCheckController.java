package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.common.service.SubjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.SelfCheck;
import edu.dlu.bysj.base.model.vo.SelfCheckDetailVo;
import edu.dlu.bysj.base.model.vo.SelfCheckVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.grade.service.SelfCheckService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author XiangXinGang
 * @date 2021/11/13 21:20
 */
@RestController
@RequestMapping(value = "/gradeManage")
@Api(tags = "自查表控制器")
@Validated
public class SelfCheckController {

    private final SelfCheckService selfCheckService;

    private final SubjectService subjectService;

    public SelfCheckController(SelfCheckService selfCheckService,SubjectService subjectService) {
        this.selfCheckService = selfCheckService;
        this.subjectService = subjectService;
    }

    @PostMapping(value = "/score/selfCheck/content")
    @LogAnnotation(content = "教师填写/修改自查表")
    @RequiresPermissions({"selfCheck:content"})
    @ApiOperation(value = "填写/修改自查表")
    public CommonResult<Object> submitSelfCheckTable(@Valid @RequestBody SelfCheckVo checkVo) {
        Subject subject = subjectService.getOne(new QueryWrapper<Subject>().eq("id", checkVo.getSubjectId()));
        Integer processCode = ProcessEnum.TEACHER_SELF_CHECK.getProcessCode();
        SelfCheck check = null;
        if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId()+1)){
            check = selfCheckService.getOne(new QueryWrapper<SelfCheck>().eq("subject_id", checkVo.getSubjectId()));
            if (ObjectUtil.isNull(check))
                check = new SelfCheck();
            check.setSubjectId(checkVo.getSubjectId());
            check.setArticleArchive(checkVo.getArticleArchive());
            check.setContent(checkVo.getContent());
            check.setResearchContent(checkVo.getResearchContent());
            check.setLiteratureReview(checkVo.getLiteratureReview());
            check.setTaskBook(checkVo.getTaskBook());
            check.setTeacherGuide(checkVo.getTeacherGuide());
            check.setPeerReview(checkVo.getPeerReview());
            check.setPaperReply(checkVo.getPaperReply());
            check.setChooseSubject(checkVo.getChooseSubject());
            check.setLiteratureApplication(checkVo.getLiteratureApplication());
            check.setTranslation(checkVo.getTranslation());
            check.setMainText(checkVo.getMainText());
            check.setCheckDate(LocalDate.now());
            subject.setProgressId(processCode);
            subjectService.updateById(subject);
        }
        return selfCheckService.saveOrUpdate(check) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @GetMapping(value = "/score/selfCheck/detail")
    @LogAnnotation(content = "查看自查表详情")
    @RequiresPermissions({"selfCheck:detail"})
    @ApiOperation(value = "查看自查表详情")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<SelfCheckDetailVo>
        obtainDetailSelfCheck(@NotNull Integer subjectId) {
        SelfCheck check = selfCheckService.getOne(new QueryWrapper<SelfCheck>().eq("subject_id", subjectId));

        SelfCheckDetailVo selfCheckDetailVo = new SelfCheckDetailVo();

        if (ObjectUtil.isNotNull(check)) {
            selfCheckDetailVo.setArticleArchive(check.getArticleArchive());
            selfCheckDetailVo.setContent(check.getContent());
            selfCheckDetailVo.setResearchContent(check.getResearchContent());
            selfCheckDetailVo.setLiteratureReview(check.getLiteratureReview());
            selfCheckDetailVo.setTaskBook(check.getTaskBook());
            selfCheckDetailVo.setTeacherGuide(check.getTeacherGuide());
            selfCheckDetailVo.setPeerReview(check.getPeerReview());
            selfCheckDetailVo.setPaperReply(check.getPaperReply());
            selfCheckDetailVo.setChooseSubject(check.getChooseSubject());
            selfCheckDetailVo.setLiteratureApplication(check.getLiteratureApplication());
            selfCheckDetailVo.setTranslation(check.getTranslation());
            selfCheckDetailVo.setMainText(check.getMainText());
            selfCheckDetailVo.setCheckDate(check.getCheckDate());
        }
        return CommonResult.success(selfCheckDetailVo);
    }
}
