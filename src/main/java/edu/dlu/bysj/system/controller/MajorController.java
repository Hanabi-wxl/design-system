package edu.dlu.bysj.system.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.vo.MajorVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.system.model.query.MajorQueryDto;
import edu.dlu.bysj.system.service.CollegeService;
import edu.dlu.bysj.system.service.MajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    private final CollegeService collegeService;


    public MajorController(MajorService majorService,CollegeService collegeService) {
        this.majorService = majorService;
        this.collegeService = collegeService;
    }

    /*
     * @Description: 教师选择学院后获取专业列表
     * @Author: sinre
     * @Date: 2022/6/18 18:19
     * @param majorQueryDto
     * @return edu.dlu.bysj.base.result.CommonResult<edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.MajorVo>>
     **/
    @GetMapping(value = "/system/major/list")
    @LogAnnotation(content = "获取专业列表")
    @RequiresPermissions({"major:list"})
    @ApiOperation(value = "获取专业列表")
    public CommonResult<TotalPackageVo<MajorVo>> majorList(
            @Valid MajorQueryDto majorQueryDto
    ) {
        Integer myCollegeId = majorQueryDto.getCollegeId();
        int pageNumber = majorQueryDto.getPageNumber() == null ? 1 : majorQueryDto.getPageNumber();
        int pageSize = majorQueryDto.getPageSize() == null ? 12 : majorQueryDto.getPageSize();
        TotalPackageVo<MajorVo> totalPackageVo =
                majorService.majorPagination(myCollegeId, pageNumber, pageSize);
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
