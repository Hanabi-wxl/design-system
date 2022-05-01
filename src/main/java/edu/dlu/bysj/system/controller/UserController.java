package edu.dlu.bysj.system.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dlu.bysj.system.model.dto.UserAccountDto;
import edu.dlu.bysj.system.model.dto.UserInitDto;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.model.entity.TeacherRole;
import edu.dlu.bysj.base.model.enums.ManagerEnum;
import edu.dlu.bysj.base.model.query.UserListQuery;
import edu.dlu.bysj.base.model.vo.AdminVo;
import edu.dlu.bysj.base.model.vo.ModifyUserVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.model.vo.UserVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.EncryptUtil;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.base.util.SimpleHashUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.TeacherService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.system.service.TeacherRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author XiangXinGang
 * @date 2021/10/16 10:14
 */
@RestController
@RequestMapping("/systemManagement")
@Api(tags = "用户管理控制器")
@Validated
public class UserController {

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final TeacherRoleService teacherRoleService;


    public static final String ONE = "1";

    public UserController(StudentService studentService, TeacherService teacherService,
                          TeacherRoleService teacherRoleService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.teacherRoleService = teacherRoleService;

    }

    @GetMapping(value = "system/user/list")
    @LogAnnotation(content = "查询用户列表")
    @RequiresPermissions({"user:list"})
    @ApiOperation(value = "根据major_id, name,userNumber 分页查询用户列表")
    public CommonResult<TotalPackageVo<UserVo>> userList(@Validated UserListQuery query) {
        // 0 老师 1学生
        if (ONE.equals(query.getIsStudent())) {
            return CommonResult.success(studentService.getStudentByUserQuery(query));
        } else {
            return CommonResult.success(teacherService.getTeacherByUserQuery(query));
        }
    }

    @GetMapping("/system/user/detail")
    @LogAnnotation(content = "查看教师详细信息")
    @RequiresPermissions({"user:detail"})
    @ApiOperation(value = "查看用户详细信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "teacherId", value = "教师id", required = true)})
    public CommonResult<Map<String, Object>> checkDetailInformation(@RequestParam("teacherId") String teacherId)
            throws JsonProcessingException {
        Map<String, Object> result = teacherService.teacherHeadDetail(teacherId);
        return CommonResult.success(result);
    }

    /*
     * @Description: 添加requestBody 获取请求体内的数据
     * @Author: sinre
     * @Date: 2022/4/26 13:02
     * @param userVo
     * @param response
     * @return edu.dlu.bysj.base.result.CommonResult<java.lang.Object>
     **/
    @PostMapping(value = "/system/user/change")
    @LogAnnotation(content = "修该用户信息")
    @RequiresPermissions({"use:change"})
    @ApiOperation(value = "修改用户信息")
    public CommonResult<Object> modifyUserInformation(@RequestBody ModifyUserVo userVo, HttpServletResponse response) {
        // 学生 1,是，2 不是
        boolean flag = false;
        /*对jwt中的信息进行跟新*/
        Map<String, Object> result = null;
        if (ONE.equals(userVo.getIsStudent())) {
            flag = studentService.changeStudentInformation(userVo);
            Student student = studentService.getOne(new QueryWrapper<Student>().eq("id", userVo.getUserId()));
            result = studentService.createJwt(student.getStudentNumber(), student);
        } else {
            flag = teacherService.changeTeacherInformation(userVo);
            Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().eq("id", userVo.getUserId()));
            result = teacherService.createJwt(teacher.getTeacherNumber(), teacher);
        }
        if (result != null && result.containsKey(JwtUtil.AUTH_HEADER)) {
            response.setHeader(JwtUtil.AUTH_HEADER, result.get(JwtUtil.AUTH_HEADER).toString());
        }

        return flag ? CommonResult.success(null) : CommonResult.failed();
    }


    @PostMapping(value = "/system/user/add")
    @LogAnnotation(content = "修该用户信息")
    @RequiresPermissions({"use:add"})
    @ApiOperation(value = "添加用户")
    public CommonResult<Object> addUser(@RequestBody ModifyUserVo userVo, HttpServletResponse response) {
        // 学生 1,是，2 不是
        boolean flag = false;
        if (ONE.equals(userVo.getIsStudent())) {
            flag = studentService.addStudent(userVo);
        } else {
            flag = teacherService.addTeacher(userVo);
        }

        return flag ? CommonResult.success(null) : CommonResult.failed();
    }


    @PostMapping(value = "/system/user/initPassword")
    @LogAnnotation(content = "修改用户密码")
    @RequiresPermissions({"user:init"})
    @ApiOperation(value = "修改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "isStudent", value = "是否为学生(1是, 0不是)", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true),
            @ApiImplicitParam(name = "userNumber", value = "学号/工号", required = true)
    })
    public CommonResult<Object> modifyUserPassword(@RequestBody UserInitDto user) {
        // 加密;
        String newPassword = user.getNewPassword();
        String userNumber = user.getUserNumber();
        Integer userId = user.getUserId();
        String isStudent = user.getIsStudent();
        String password = EncryptUtil.encrypt(newPassword, userNumber);
        boolean flag = false;
        /*1,学生, 0 教师*/
        if (ONE.equals(isStudent)) {
            flag =
                studentService.update(
                        null, new UpdateWrapper<Student>().set("password", password).eq("id", userId));
        } else {
            flag =
                teacherService.update(
                        null, new UpdateWrapper<Teacher>().set("password", password).eq("id", userId));
        }
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @GetMapping(value = "/system/user/changeStatus")
    @LogAnnotation(content = "修改用户状态")
    @RequiresPermissions({"user:changeStatus"})
    @ApiOperation(value = "修改用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "isStudents", value = "是否学生(1:是,0 ：不是)", required = true),
            @ApiImplicitParam(name = "canUse", value = "用户的使用状态", required = true)
    })
    public CommonResult<Object> modifyUserCanUse(
            @RequestParam("userId") String userId,
            @RequestParam("isStudents") String isStudents,
            @RequestParam("canUse") String canUse) {
        boolean flag = false;
        /*1,学生, 0 不是学生*/
        if (ONE.equals(isStudents)) {
            flag =
                    studentService.update(
                            new UpdateWrapper<Student>().set("can_use", canUse).eq("id", userId));
        } else {
            flag =
                    teacherService.update(
                            new UpdateWrapper<Teacher>().set("can_use", canUse).eq("id", userId));
        }
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @GetMapping(value = "/system/user/changeRole")
    @LogAnnotation(content = "新增/删除教师角色")
    @RequiresPermissions({"user:changeRole"})
    @ApiOperation(value = "新增/删除教师角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teacherId", value = "教师id", required = true),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true),
            @ApiImplicitParam(name = "type", value = "操作类型(1:增加，0：删除)")
    })
    public CommonResult<Object> modifyTeacherRole(
            @RequestParam("teacherId") Integer teacherId,
            @RequestParam("roleId") Integer roleId,
            @RequestParam("type") String type) {
        boolean flag = false;
        /*1,新增, 0 删除*/
        if (ONE.equals(type)) {
            TeacherRole teacherRole = new TeacherRole();
            teacherRole.setRoleId(roleId);
            teacherRole.setTeacherId(teacherId);
            flag = teacherRoleService.save(teacherRole);
        } else {
            flag = teacherRoleService.remove(
                    new QueryWrapper<TeacherRole>().eq("role_id", roleId).eq("teacher_id", teacherId));
        }
        return flag ? CommonResult.success(null) : CommonResult.failed();
    }

    @GetMapping(value = "/system/user/roleByTeacherId")
    @LogAnnotation(content = "获取教师的角色信息")
    @RequiresPermissions({"user:roles"})
    @ApiOperation(value = "根据教师id 获取教师的角色信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "teacherId", value = "教师id", required = true)})
    public CommonResult<Set<Integer>> obtainTeacherRoles(@RequestParam("teacherId") String teacherId) {
        Set<Integer> result = teacherRoleService.teacherRoleName(teacherId);
        return CommonResult.success(result);
    }

    @GetMapping(value = "/system/user/manager")
    @LogAnnotation(content = "查看管理员列表")
    @RequiresPermissions({"user:manager"})
    @ApiOperation(value = "管理员列表")
    public CommonResult<Map<String, Object>> managerList(HttpServletRequest request) {
        String token = request.getHeader("jwt");
        List<Integer> roleIds = JwtUtil.getRoleIds(token);
        // 判断和三种主要的交集; 专业管理员()，学院管理员,校级管理员
        int integer = 0;
        if (roleIds != null && !roleIds.isEmpty()) {
            integer = roleIds.stream().max((a, b) -> a - b).get();
        }
        integer = Math.min(integer, 6);
        List<Integer> param = new ArrayList<>();

        for (int i = 3; i < integer; i++) {
            param.add(i);
        }

        Map<String, Object> result = new HashMap<>(16);

        // 查询拥有参数中权限的所有人;
        for (Integer value : param) {
            String key = ManagerEnum.mangerRoleName(value);
            List<AdminVo> adminVos = teacherRoleService.teacherMangerRoles(value, JwtUtil.getMajorId(token));
            result.put(key, adminVos);
        }

        return CommonResult.success(result);
    }

    @PostMapping(value = "/system/user/judgeOldPassword")
    @LogAnnotation(content = "判断密码是否正确")
    @RequiresPermissions({"user:judge"})
    @ApiOperation(value = "判断输入密码是否正确接口")
    public CommonResult<Object> judgePassword(HttpServletRequest request, @RequestBody String password) {
        String pass = JSONUtil.parseObj(password).get("password", String.class);
        String token = request.getHeader("jwt");
        // 获取学生/教师id;
        String userType = JwtUtil.getUserType(token);
        String userNumber = JwtUtil.getUserNumber(token);
        Integer userId = JwtUtil.getUserId(token);
        String pwd = EncryptUtil.encrypt(pass, userNumber);
        boolean flag = false;
        /*jwt中保存的userType 1, 教师, 0 学生*/
        if (ONE.equals(userType)) {
            // 教师
            Teacher teacher = teacherService.getById(userId);
            Optional<Teacher> teacherOptional = Optional.ofNullable(teacher);
            if (teacherOptional.isPresent()) {
                flag = pwd.equals(teacher.getPassword());
            }
        } else {
            // 学生
            Student student = studentService.getById(userId);
            Optional<Student> studentOptional = Optional.ofNullable(student);
            if (studentOptional.isPresent()) {
                flag = pwd.equals(student.getPassword());
            }
        }
        return flag ? CommonResult.success(null, "密码正确") : CommonResult.failed("密码错误");
    }

    @PostMapping(value = "/system/user/judgeOldPasswordById")
    @LogAnnotation(content = "判断密码是否正确")
    @RequiresPermissions({"user:judgex"})
    @ApiOperation(value = "判断输入他人密码是否正确接口")
    public CommonResult<Object> judgePassword(HttpServletRequest request, @RequestBody UserAccountDto accountDto) {
        // 获取学生/教师id;
        Integer userType = accountDto.getIsStudent();
        Integer userId = accountDto.getUserId();
        String pwd = accountDto.getPassword();
        boolean flag = false;
        // isStudent: 0为教师
        if (userType == 0) {
            // 教师
            Teacher teacher = teacherService.getById(userId);
            Optional<Teacher> teacherOptional = Optional.ofNullable(teacher);
            if (teacherOptional.isPresent()) {
                String password = SimpleHashUtil.generatePassword(pwd, teacher.getTeacherNumber());
                flag = password.equals(teacher.getPassword());
            }
        } else {
            // 学生
            Student student = studentService.getById(userId);
            Optional<Student> studentOptional = Optional.ofNullable(student);
            if (studentOptional.isPresent()) {
                String password = SimpleHashUtil.generatePassword(pwd, student.getStudentNumber());
                flag = password.equals(student.getPassword());
            }
        }
        return flag ? CommonResult.success(null, "密码正确") : CommonResult.failed("密码错误");
    }
}
