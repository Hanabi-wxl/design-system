package edu.dlu.bysj.defense.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.EachMark;
import edu.dlu.bysj.base.model.query.MutualEvaluationQuery;
import edu.dlu.bysj.base.model.vo.MutualEvaluationVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.defense.service.EachMarkService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/6 9:24
 */
@RestController
@RequestMapping(value = "/defenseManagement")
@Api(tags = "互评管理控制器")
@Validated
public class MutualEvaluationController {

    private final EachMarkService eachMarkService;

    @Autowired
    public MutualEvaluationController(EachMarkService eachMarkService) {
        this.eachMarkService = eachMarkService;
    }

    @GetMapping(value = "/defence/other/list")
    @LogAnnotation(content = "查看互评分组列表")
    @RequiresPermissions({"mutual:list"})
    @ApiOperation(value = "获取互评分组列表")
    public CommonResult<TotalPackageVo<MutualEvaluationVo>> mutualEvaluationList(@Valid MutualEvaluationQuery query) {
        TotalPackageVo<MutualEvaluationVo> result = eachMarkService.eachMarkMajorInfo(query);
        return CommonResult.success(result);
    }

    @GetMapping(value = "/defence/other/changeOtherTeacher/{subjectId}/{teacherId}")
    @LogAnnotation(content = "修改互评教师")
    @RequiresPermissions({"mutual:change"})
    @ApiOperation(value = "修改互评教师")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId", value = "题目id"),
            @ApiImplicitParam(name = "teacherId", value = "教师id")
    })
    public CommonResult<Object> modifyEachMarkTeacher(@PathVariable("subjectId") @NotNull Integer subjectId,
                                                      @PathVariable("teacherId") @NotNull Integer teacherId) {
        String message = "没有该题目";
        EachMark subjectEachMark = eachMarkService.getOne(new QueryWrapper<EachMark>().eq("subject_id", subjectId));
        if (ObjectUtil.isNotNull(subjectEachMark)) {
            subjectEachMark.setTeacherId(teacherId);
            eachMarkService.updateById(subjectEachMark);
            message = "修改成功";
        }
        return CommonResult.success(message);
    }


    @GetMapping(value = "/defence/other/generate/{majorId}/{grade}/{printout}/{time}")
    @LogAnnotation(content = "管理员按专业生成互评数据")
    @RequiresPermissions({"mutual:generate"})
    @ApiOperation(value = "生成互评数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "majorId", value = "专业id", required = true),
            @ApiImplicitParam(name = "grade", value = "年份", required = true),
            @ApiImplicitParam(name = "printout", value = "是否提交打印稿(1,提交,0不提交)", required = true),
            @ApiImplicitParam(name = "time", value = "提交打印稿的时间")
    })
    public CommonResult<Object> generateEachMarkDate(@PathVariable("majorId") @NotNull Integer majorId,
                                                     @PathVariable("grade") @NotNull Integer year,
                                                     @PathVariable("printout") @NotNull Integer printout,
                                                     @PathVariable("time") String time,
                                                     HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        if (!StringUtils.isEmpty(jwt)) {
            /*generateEachMarkTeacher 方法使用事务任何的异常都会使得事务回滚,用是否抛出异常来判断该方法是否执行成功*/
            try {
                boolean flag = eachMarkService.generateEachMarkTeacher(majorId, year, printout, time, JwtUtil.getUserId(jwt));
                return flag ? CommonResult.success("执行成功") : CommonResult.success("该专业" + year + "级暂时没有数据");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return CommonResult.failed("操作失败");
    }

    @GetMapping(value = "/defence/other/studentInfo/{subjectId}")
    @LogAnnotation(content = "学生查看该题目的互评教师信息")
    @RequiresPermissions({"mutual:studentInfo"})
    @ApiOperation(value = "学生获取互评教师信息")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<Map<String, Object>> eachMarkTeacherInfo(@PathVariable("subjectId") @NotNull Integer subjectId) {
        Map<Integer, Map<String, Object>> result = eachMarkService.selectEachMarkTeacherBySubject(subjectId);
        return CommonResult.success(result.get(subjectId));
    }
    
}
