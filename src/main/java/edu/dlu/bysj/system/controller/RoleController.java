package edu.dlu.bysj.system.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Function;
import edu.dlu.bysj.base.model.entity.Role;
import edu.dlu.bysj.base.model.entity.RoleFunction;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.system.model.dto.RoleFunctionDto;
import edu.dlu.bysj.system.model.vo.RoleMap;
import edu.dlu.bysj.system.service.FunctionService;
import edu.dlu.bysj.system.service.RoleFunctionService;
import edu.dlu.bysj.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/18 9:09
 */
@RestController
@RequestMapping("systemManagement")
@Api(tags = "角色管理控制器")
public class RoleController {
    private final RoleService roleService;
    private static final String ONE = "1";
    private static final String ZERO = "0";


    private final RedisTemplate redisTemplate;

    private final RoleFunctionService roleFunctionService;

    private final FunctionService functionService;

    public RoleController(RoleService roleService, RedisTemplate redisTemplate, RoleFunctionService roleFunctionService, FunctionService functionService) {
        this.roleService = roleService;
        this.redisTemplate = redisTemplate;
        this.roleFunctionService = roleFunctionService;
        this.functionService = functionService;
    }

    @GetMapping(value = "system/role/list")
    @LogAnnotation(content = "查看角色列表")
    @RequiresPermissions({"role:list"})
    @ApiOperation(value = "查看角色列表")
    public CommonResult<List<Role>> roleList() {
        String key = RedisKeyEnum.TOTAL_ROLE_KEY.getKeyValue();
        List<Role> result = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 范围为 -1 -1 查看全部的值
            result = redisTemplate.opsForList().range(key, 0, -1);
        } else {
            result = roleService.list();
            redisTemplate.opsForList().rightPushAll(key, result);
            // 设置10天过期时间
            redisTemplate.expire(key, 10, TimeUnit.DAYS);
        }
        return CommonResult.success(result);
    }

    @PostMapping(value = "/system/role/add")
    @LogAnnotation(content = "新建角色")
    @RequiresPermissions({"role:add"})
    @ApiOperation(value = "新建角色")
    public CommonResult<Object> roleAdd(@RequestBody Role role) {
        // 修改
        return roleService.save(role) ? CommonResult.success(null) : CommonResult.failed();
    }


    @DeleteMapping(value = "/system/role/delete")
    @LogAnnotation(content = "根据roleId删除对应的角色")
    @RequiresPermissions({"role:delete"})
    @ApiOperation(value = "根据roleId删除该对应的角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleId", value = "角色id", required = true)})
    public CommonResult<Object> roleDelete(@RequestBody String roleId) {
        String id = JSONUtil.parseObj(roleId).get("roleId", String.class);
        // 删除对应的角色;
        return roleService.removeById(id) ? CommonResult.success(null) : CommonResult.failed();
    }


    @GetMapping(value = "/system/role/hasFunction")
    @LogAnnotation(content = "获取角色已有功能")
    @RequiresPermissions({"role:function"})
    public CommonResult<Map<String, Object>> roleFunctions(String roleId) {
        List<Integer> list = roleFunctionService.ObtainFunctionIds(roleId);
        Map<String, Object> result = new HashMap<>(16);
        result.put("functionId", list);
        return CommonResult.success(result);
    }

    /*
     * @Description:
     * @Author: sinre
     * @Date: 2022/8/31 10:43
     * @param
     * @return edu.dlu.bysj.base.result.CommonResult<java.util.List<java.util.List<edu.dlu.bysj.system.model.vo.RoleMap>>>
     **/
    @GetMapping(value = "/system/role/functionList")
    @LogAnnotation(content = "获取全部功能")
    @RequiresPermissions({"role:function"})
    public CommonResult<List<List<RoleMap>>> functionsList() {
        List<Function> functionList = functionService.list(new QueryWrapper<Function>().select("id", "name", "parent_id"));
        List<List<RoleMap>> ans = new ArrayList<>();
        for (Function listItem : functionList) {
            if(listItem.getParentId() == -1) {
                List<RoleMap> ansItemList = new ArrayList<>();
                RoleMap ansItem = new RoleMap();
                ansItem.setId(-1);
                ansItem.setTitle(listItem.getName());
                ansItemList.add(ansItem);
                for (Function item : functionList) {
                    if(item.getParentId().equals(listItem.getId())) {
                        RoleMap ansItem1 = new RoleMap();
                        ansItem1.setId(item.getId());
                        ansItem1.setTitle(item.getName());
                        ansItemList.add(ansItem1);
                    }
                }
                ans.add(ansItemList);
            }
        }
        return CommonResult.success(ans);
    }


    @PatchMapping(value = "/system/role/change")
    @LogAnnotation(content = "修改角色对应的菜单")
    @RequiresPermissions({"role:change"})
    @ApiOperation(value = "修改角色对应的菜单")
    public CommonResult<Object> roleFunctionModify(@RequestBody RoleFunctionDto dto) {
        Integer functionId = dto.getFunctionId();
        Integer roleId = dto.getRoleId();
        // 1新增, 0删除
        String type = dto.getType();
        boolean flag = false;
        // 新增
        if (ONE.equals(type)) {
            RoleFunction roleFunction = new RoleFunction();
            roleFunction.setRoleId(roleId);
            roleFunction.setFunctionId(functionId);
            flag = roleFunctionService.save(roleFunction);
        } else if (ZERO.equals(type)) {
            // 删除(两个外键组合为一个唯一的主键)
            flag = roleFunctionService.remove(
                    new QueryWrapper<RoleFunction>().eq("role_id", roleId).eq("function_id", functionId));
        }
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }
}
