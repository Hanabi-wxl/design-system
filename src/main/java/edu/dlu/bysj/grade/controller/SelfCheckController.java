package edu.dlu.bysj.grade.controller;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    public SelfCheckController(SelfCheckService selfCheckService) {
        this.selfCheckService = selfCheckService;
    }

    @PostMapping(value = "/score/selfChek/content")
    @LogAnnotation(content = "教师填写/修改自查表")
    @RequiresPermissions({"selfCheck:content"})
    @ApiOperation(value = "填写/修改自查表")
    public CommonResult<Object> submitSelfCheckTable(@Valid SelfCheckVo checkVo) {
        SelfCheck check =
            selfCheckService.getOne(new QueryWrapper<SelfCheck>().eq("subject_id", checkVo.getSubjectId()));
        if (ObjectUtil.isNull(check)) {
            check = new SelfCheck();
        }
        check.setSubjectId(checkVo.getSubjectId());
        check.setArticleArchive(checkVo.getArticleArchive());
        check.setContent(checkVo.getContent());
        check.setReaserchContent(checkVo.getReaserchContent());
        check.setLiteratureReview(checkVo.getLiteratureReview());
        check.setTaskbook(checkVo.getTaskbook());
        check.setTeacherGuide(checkVo.getTeacherGuide());
        check.setPeerReview(checkVo.getPeerReview());
        check.setPaperReply(checkVo.getPaperReply());
        check.setChooseSubject(checkVo.getChooseSubject());
        check.setLiteratureApplication(checkVo.getLiteratureApplication());
        check.setTranslation(checkVo.getTranslation());
        check.setMainText(checkVo.getMainText());
        check.setCheckDate(LocalDate.now());
        return selfCheckService.saveOrUpdate(check) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @GetMapping(value = "/score/selfCheck/detail/{subjectId}")
    @LogAnnotation(content = "查看自查表详情")
    @RequiresPermissions({"selfCheck:detail"})
    @ApiOperation(value = "查看自查表详情")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<SelfCheckDetailVo>
        obtainDetailSelfCheck(@PathVariable("subjectId") @NotNull Integer subjectId) {
        SelfCheck check = selfCheckService.getOne(new QueryWrapper<SelfCheck>().eq("subject_id", subjectId));

        SelfCheckDetailVo selfCheckDetailVo = new SelfCheckDetailVo();

        if (ObjectUtil.isNotNull(check)) {
            selfCheckDetailVo.setArticleArchive(check.getArticleArchive());
            selfCheckDetailVo.setContent(check.getContent());
            selfCheckDetailVo.setReaserchContent(check.getReaserchContent());
            selfCheckDetailVo.setLiteratureReview(check.getLiteratureReview());
            selfCheckDetailVo.setTaskbook(check.getTaskbook());
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
