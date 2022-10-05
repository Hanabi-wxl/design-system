package edu.dlu.bysj.system.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Class;
import edu.dlu.bysj.base.model.vo.ModifyClass;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.system.service.ClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/22 16:00
 */
@RestController
@RequestMapping(value = "/systemManagement")
@Api(tags = "班级管理控制器")
public class ClassController {
    private final ClassService classService;

    @Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping(value = "/system/class/list")
    @LogAnnotation(content = "查询班级列表根据专业id")
    @RequiresPermissions({"class:list"})
    @ApiOperation(value = "查询班级列表(根据专业id)")
    public CommonResult<TotalPackageVo<Class>> classList(@RequestParam Integer major, @RequestParam Integer year) {
        TotalPackageVo<Class> res = new TotalPackageVo<>();
        List<Class> classes = classService.list(new QueryWrapper<Class>().eq("major_id", major).eq("grade",GradeUtils.getGrade(year)));
        res.setArrays(classes);
        res.setTotal(classes.size());
        return CommonResult.success(res);
    }

    @PostMapping(value = "/system/class/addOrChange")
    @LogAnnotation(content = "修改班级信息")
    @RequiresPermissions({"class:add"})
    @ApiOperation(value = "修改班级信息")
    public CommonResult<Object> modifyClassInfo(@RequestBody ModifyClass modifyClass) {
        boolean flag;
        Class classInfo = new Class();
        classInfo.setId(modifyClass.getClassId());
        classInfo.setGrade(GradeUtils.getGrade(modifyClass.getGrade()));
        classInfo.setName(modifyClass.getName());
        classInfo.setMajorId(modifyClass.getMajorId());
        flag = classService.saveOrUpdate(classInfo);
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @DeleteMapping(value = "/system/class/delete")
    @ApiOperation(value = "通过classId删除class")
    @LogAnnotation(content = "删除班级信息")
    @RequiresPermissions({"class:delete"})
    @ApiImplicitParam(name = "classId", value = "班级id")
    public CommonResult<Object> deleteClass(@RequestBody String classId) {
        Integer id = JSONUtil.parseObj(classId).get("classId", Integer.class);
        return classService.removeById(id) ? CommonResult.success(null) : CommonResult.failed();
    }
}
