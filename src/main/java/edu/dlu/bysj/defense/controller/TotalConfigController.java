package edu.dlu.bysj.defense.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.TotalConfig;
import edu.dlu.bysj.base.model.vo.ModifyTotalConfigVo;
import edu.dlu.bysj.base.model.vo.basic.BasicTotalConfigVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.defense.service.TotalConfigService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/11/8 16:56
 */
@RestController
@RequestMapping(value = "/defenseManagement")
@Api(tags = "总配置控制器")
@Validated
public class TotalConfigController {

    private final TotalConfigService totalConfigService;

    @Autowired
    public TotalConfigController(TotalConfigService totalConfigService) {
        this.totalConfigService = totalConfigService;
    }

    @GetMapping(value = "/defence/totalConfig/list/{grade}/{majorId}")
    @LogAnnotation(content = "获取专业总配置列表")
    @RequiresPermissions({"config:list"})
    @ApiOperation(value = "获取总配置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grade", value = "年级"),
            @ApiImplicitParam(name = "majorId", value = "专业id")
    })
    public CommonResult<BasicTotalConfigVo> obtainTotalConfig(@PathVariable("grade") @NotNull Integer grade,
                                                              @PathVariable("majorId") @NotNull Integer majorId) {
        TotalConfig config = totalConfigService.getOne(new QueryWrapper<TotalConfig>()
                .eq("grade", grade)
                .eq("major_id", majorId));
        BasicTotalConfigVo totalConfigVo = null;
        if (ObjectUtil.isNotNull(config)) {
            totalConfigVo = new BasicTotalConfigVo();
            totalConfigVo.setDefenceRule(config.getDefenseRule());
            totalConfigVo.setApplyDefence(config.getApplyDefense());
            totalConfigVo.setOpenReportRule(config.getOpenreportRule());
            totalConfigVo.setScoreRule(config.getOpenreportRule());
            totalConfigVo.setEachMark(config.getEachmark());
            totalConfigVo.setDesign(config.getDesign());
        }
        return CommonResult.success(totalConfigVo);
    }

    @PostMapping(value = "/defence/totalConfig/addOrChange")
    @LogAnnotation(content = "修改/新增专业总配置")
    @RequiresPermissions({"config:add"})
    @ApiOperation(value = "修改/新增专业总配置")
    public CommonResult<Object> modifyTotalConfig(@Valid ModifyTotalConfigVo configVo) {

        TotalConfig totalConfig = new TotalConfig();
        totalConfig.setGrade(configVo.getGrade());
        totalConfig.setMajorId(configVo.getMajorId());
        totalConfig.setDefenseRule(configVo.getDefenceRule());
        totalConfig.setApplyDefense(configVo.getApplyDefence());
        totalConfig.setOpenreportRule(configVo.getOpenReportRule());
        totalConfig.setScoreRule(configVo.getScoreRule());
        totalConfig.setEachmark(configVo.getEachMark());
        totalConfig.setDesign(configVo.getDesign());
        if (ObjectUtil.isNotNull(configVo.getConfigId())) {
            totalConfig.setId(configVo.getConfigId());
        }
        return totalConfigService.saveOrUpdate(totalConfig) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }
}
