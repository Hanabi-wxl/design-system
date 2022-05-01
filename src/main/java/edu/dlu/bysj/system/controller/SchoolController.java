package edu.dlu.bysj.system.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.dlu.bysj.base.model.entity.School;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.system.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

/**
 * @author XiangXinGang
 * @date 2021/10/17 22:33
 */
@RestController
@RequestMapping(value = "/systemManagement")
@Api(tags = "学校管理控制器")
public class SchoolController {
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping(value = "/system/school")
    @ApiOperation(value = "获取学校列表")
    @LogAnnotation(content = "获取学校列表")
    @RequiresPermissions({"school:list"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true),
            @ApiImplicitParam(name = "pageNumber", value = "当前页数", required = true)
    })
    public CommonResult<TotalPackageVo<School>> schoolList(
            @RequestParam("pageSize") Long pageSize, @RequestParam("pageNumber") Long pageNumber) {

        Page<School> page = new Page(pageNumber, pageSize);
        Page<School> iPage = schoolService.page(page);
        TotalPackageVo<School> packageVo = new TotalPackageVo<>();
        long total = iPage.getTotal();
        packageVo.setTotal((int) total);
        packageVo.setArrays(iPage.getRecords());

        return CommonResult.success(packageVo);
    }

    @PostMapping(value = "system/school/addOrChange")
    @LogAnnotation(content = "跟新或新增学校")
    @RequiresPermissions({"school:add"})
    @ApiOperation(value = "跟新或新增学校")
    public CommonResult<Object> schoolModifyAndAdd(@RequestBody School school) {
        System.out.println(school.toString());
        String key = RedisKeyEnum.SCHOOL_NAME_ID_KEY.getKeyValue();
        if (schoolService.saveOrUpdate(school)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @DeleteMapping(value = "/system/school/delete")
    @LogAnnotation(content = "删除学校")
    @RequiresPermissions({"school:delete"})
    @ApiOperation(value = "删除学校")
    public CommonResult<Object> schoolDelete(@RequestBody String schoolId) {
        JSONObject jsonObject = JSONUtil.parseObj(schoolId);
        Integer id = jsonObject.get("schoolId", Integer.class);
        return schoolService.removeById(id) ? CommonResult.success(null) : CommonResult.failed();
    }
}
