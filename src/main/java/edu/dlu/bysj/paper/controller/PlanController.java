package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.Plan;
import edu.dlu.bysj.base.model.vo.ContentVo;
import edu.dlu.bysj.base.model.vo.PlanSubmitVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.PlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/2 17:16
 */
@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "计划管理控制器")
public class PlanController {

    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    /*接口的数据结构不是很好描述使用json 描述*/
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/plan/submitContent")
    @RequiresPermissions({"plan:submitContent"})
    @ApiOperation(value = "提交周计划内容")
    public CommonResult<Object> submitWeekPlans(@RequestBody @Valid PlanSubmitVo weekPlans) {
        List<Plan> plansValue = new ArrayList<>();

        if (ObjectUtil.isNotNull(weekPlans.getContent())) {
            for (ContentVo element : weekPlans.getContent()) {
                Plan plan = new Plan();
                plan.setSubjectId(weekPlans.getSubjectId());
                plan.setWeek(element.getWeek());
                plan.setContent(element.getContent());
                plansValue.add(plan);
            }
        }

        return planService.saveBatch(plansValue) ? CommonResult.success("执行成功") : CommonResult.failed("执行失败");
    }

    @GetMapping(value = "/plan/detail")
    @LogAnnotation(content = "查看题目计划表详情")
    @RequiresPermissions({"plan:detail"})
    @ApiOperation(value = "根据题目id查看计划表详情")
    public CommonResult<List<ContentVo>> checkStudentPlans(Integer subjectId) {
        List<ContentVo> contents = planService.checkPlans(subjectId);
        return CommonResult.success(contents);
    }
}
