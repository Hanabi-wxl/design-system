package edu.dlu.bysj.system.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.vo.MajorVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.system.query.MajorQueryDto;
import edu.dlu.bysj.system.service.MajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * @author XiangXinGang
 * @date 2021/10/21 20:27
 */
@RestController
@RequestMapping(value = "/systemManagement")
@Api(tags = "专业控制器")
@Validated
public class MajorController {
    private final MajorService majorService;


    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping(value = "/system/major/list")
    @LogAnnotation(content = "获取专业列表")
    @RequiresPermissions({"major:list"})
    @ApiOperation(value = "获取专业列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "collegeId", value = "学院id"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数"),
            @ApiImplicitParam(name = "pageNumber", value = "当前页码")
    })
    public CommonResult<TotalPackageVo<MajorVo>> majorList(
            MajorQueryDto majorQueryDto
//            @RequestParam(value = "collegeId") String collegeId,
//            @RequestParam(value = "pageSize")
//            @Min(value = 1, message = "每页记录数的长度必须不小于1") String pageSize,
//            @RequestParam(value = "pageNumber")
//            @Min(value = 1, message = "当前页码的长度必须不小于等于1") String pageNumber
    ) {
        String collegeId = majorQueryDto.getCollegeId().equals("") ? "1" : majorQueryDto.getCollegeId();
        int pageNumber = majorQueryDto.getPageNumber() == null ? 1 : majorQueryDto.getPageNumber();
        int pageSize = majorQueryDto.getPageSize() == null ? 12 : majorQueryDto.getPageSize();
        TotalPackageVo<MajorVo> totalPackageVo =
                majorService.majorPagination(collegeId, pageNumber, pageSize);
        return CommonResult.success(totalPackageVo);
    }

    @PostMapping(value = "/system/major/addOrChange")
    @LogAnnotation(content = "新增/修改major")
    @RequiresPermissions({"major:add"})
    @ApiOperation(value = "新增/修改major")
    public CommonResult<Object> majorModify(@RequestBody Major major) {
        boolean flag = majorService.saveOrUpdate(major);
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @DeleteMapping(value = "/system/major/delete")
    @LogAnnotation(content = "根据majorId删除专业")
    @RequiresPermissions({"major:delete"})
    @ApiOperation(value = "根据majorId删除专业")
    public CommonResult<Object> delete(@RequestBody String majorId) {
        JSONObject jsonObject = JSONUtil.parseObj(majorId);
        Integer id = jsonObject.get("majorId", Integer.class);
        boolean flag = majorService.removeById(id);
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }
}
