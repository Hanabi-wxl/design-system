package edu.dlu.bysj.system.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.dlu.bysj.base.model.entity.College;
import edu.dlu.bysj.base.model.query.basic.CommonPage;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.system.service.CollegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author XiangXinGang
 * @date 2021/10/18 16:31
 */
@RestController
@RequestMapping(value = "/systemManagement")
@Api(tags = "学院管理控制器")
@Validated
public class CollegeController {
    private final CollegeService collegeService;


    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    /*
     * @Description:  根据前端传入的id获取学院
     * @Author: sinre
     * @Date: 2022/4/27 14:36
     * @param page
     * @param request
     * @param schoolId
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.entity.College>>
     **/
    @GetMapping(value = "/system/college/list")
    @LogAnnotation(content = "查询学院列表根据学院id")
    @RequiresPermissions({"college:list"})
    @ApiOperation(value = "查询学院列表(根据学校id)")
    public CommonResult<TotalPackageVo<College>> collegeList(
            @Valid CommonPage page, HttpServletRequest request) {
        String token = request.getHeader("jwt");
        Integer schoolId = JwtUtil.getSchoolId(token);
        // 根据学校id查询学院并进行分页;
        TotalPackageVo<College> colleges = collegeService.collegePagination(page, schoolId);
        return CommonResult.success(colleges);
    }

    @PostMapping(value = "system/college/addOrChange")
    @LogAnnotation(content = "通过教师所在学校id，新增/修改学院")
    @RequiresPermissions({"college:add"})
    @ApiOperation(value = "通过教师所在学校id，新增/修改学院")
    public CommonResult<Object> collegeAdd(@RequestBody College college) {
        // 修改/新增;
        boolean flag = collegeService.saveOrUpdate(college);
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @DeleteMapping(value = "/system/college/delete")
    @LogAnnotation(content = "根据 collegeId 删除操作人所属学校的学院")
    @RequiresPermissions({"college:delete"})
    @ApiOperation(value = "根据 collegeId 删除操作人所属学校的学院")
    public CommonResult<Object> collegeDelete(
            @RequestBody Integer collegeId) {
        JSONObject jsonObject = JSONUtil.parseObj(collegeId);
        Integer id = jsonObject.get("collegeId", Integer.class);
        return collegeService.removeById(id)
                ? CommonResult.success(null)
                : CommonResult.failed();
    }
}
