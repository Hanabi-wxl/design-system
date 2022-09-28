package edu.dlu.bysj.paper.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Plan;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.vo.ContentVo;
import edu.dlu.bysj.base.model.vo.PlanSubmitVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.PlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class PlanController {

    private final PlanService planService;
    private final SubjectService subjectService;

    @Autowired
    public PlanController(PlanService planService, SubjectService subjectService) {
        this.planService = planService;
        this.subjectService = subjectService;
    }

    /*
     * @Description: 提交周计划内容
     * @Author: sinre
     * @Date: 2022/6/25 17:55
     * @param weekPlans
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/plan/submitContent")
    @RequiresPermissions({"plan:submitContent"})
    @ApiOperation(value = "提交周计划内容")
    public CommonResult<Object> submitWeekPlans(@RequestBody @Valid PlanSubmitVo weekPlans) {
        boolean flag = false;
        Integer subjectId = weekPlans.getSubjectId();
        Integer progressId = subjectService.getById(subjectId).getProgressId();
        Integer processCode = ProcessEnum.CHOOSE_TOPIC.getProcessCode();
        if(processCode.equals(progressId)) {
            List<Plan> plansValue = new ArrayList<>();
            int count = planService.count(new QueryWrapper<Plan>().eq("subject_id", weekPlans.getSubjectId()));
            if (count == 20)
                planService.remove(new QueryWrapper<Plan>().eq("subject_id", weekPlans.getSubjectId()));
            if (ObjectUtil.isNotNull(weekPlans.getContent())) {
                for (ContentVo element : weekPlans.getContent()) {
                    Plan plan = new Plan();
                    plan.setSubjectId(weekPlans.getSubjectId());
                    plan.setWeek(element.getWeek());
                    plan.setContent(element.getContent());
                    plansValue.add(plan);
                }
            }
            flag = planService.saveBatch(plansValue);
        } else {
            return CommonResult.failed("该题目不在本阶段内");
        }

        return flag ? CommonResult.success("执行成功") : CommonResult.failed("执行失败");
    }

    /*
     * @Description: 查看题目计划表详情
     * @Author: sinre
     * @Date: 2022/6/25 17:55
     * @param subjectId
     * @return edu.dlu.bysj.base.result.CommonResult<java.util.List<edu.dlu.bysj.base.model.vo.ContentVo>>
     **/
    @GetMapping(value = "/plan/detail")
    @LogAnnotation(content = "查看题目计划表详情")
    @RequiresPermissions({"plan:detail"})
    @ApiOperation(value = "根据题目id查看计划表详情")
    public CommonResult<List<ContentVo>> checkStudentPlans(@Valid @NotNull(message = "课题信息不能为空") Integer subjectId) {
        List<ContentVo> contents = planService.checkPlans(subjectId);
        return CommonResult.success(contents);
    }
}
