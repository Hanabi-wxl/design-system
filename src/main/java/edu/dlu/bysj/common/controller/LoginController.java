package edu.dlu.bysj.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.result.ResultCodeEnum;
import edu.dlu.bysj.base.util.IpUtil;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.base.util.SimpleHashUtil;
import edu.dlu.bysj.common.entity.JudgeType;
import edu.dlu.bysj.common.entity.LoginUser;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.TeacherService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.log.enums.LogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 该类需要跨域注解其他的类,在拦截器中跨域配置
 *
 * @author XiangXinGang
 * @date 2021/10/12 16:17
 */
@RestController
@RequestMapping("/common")
@Api(tags = "登录相关接口")
@Slf4j
@CrossOrigin
public class LoginController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public LoginController(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @PostMapping(value = "/login/student")
    @LogAnnotation(content = "学生登录", type = LogType.LOG_IN_TYPE)
    @ApiOperation(value = "学生登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "学号", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    public CommonResult<Object> studentLogin(@RequestBody
               LoginUser user, HttpServletRequest request, HttpServletResponse response) {

        String username = user.getUsername();
        String password = user.getPassword();
        String message = null;

        Student student = studentService.getOne(new QueryWrapper<Student>().eq("student_number", username));

        Map<String, Object> result = new HashMap<>();
        String originPassword = SimpleHashUtil.generatePassword(password, username);

        if (student == null) {
            result.put("status", 400);
            result.put("msg", "账号不存在");
        } else if (!originPassword.equals(student.getPassword())){
            result.put("status", 400);
            result.put("msg", "密码错误");
        } else {
            //生成token，并保存到数据库
            result = studentService.createJwt(username, student);
            result.put("status", 200);
            message = "登陆成功";
            List<Integer> roles = new LinkedList<>();
            roles.add(1);
            result.put("roleIds", roles);

            // 更新ip地址
            String ipAddress = IpUtil.getIpAddress(request);
            if ("".equals(ipAddress) || !studentService.update(new UpdateWrapper<Student>().set("recent_ip", ipAddress).eq("id", student.getId()))) {
                message = "ip地址异常";
            }
        }

        if ((Integer) result.get("status") == 400)
            return CommonResult.failed(result.get("msg").toString());

        return CommonResult.success(result, message);
    }

    @PostMapping(value = "/login/teacher")
    @LogAnnotation(content = "教师登录", type = LogType.LOG_IN_TYPE)
    @ApiOperation(value = "老师登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "教工号", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    public CommonResult<Object> teacherLogin( @RequestBody @Valid
            LoginUser user, HttpServletRequest request, HttpServletResponse response) {

        String username = user.getUsername();
        String password = user.getPassword();
        String message = null;

        Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_number", username));

        Map<String, Object> result = new HashMap<>();
        String originPassword = SimpleHashUtil.generatePassword(password, username);

        if (teacher == null) {
            result.put("status", 400);
            result.put("msg", "账号不存在");
        } else if(!originPassword.equals(teacher.getPassword())){
            result.put("status", 400);
            result.put("msg", "密码错误");
        }else {
            //生成token，并保存到数据库
            result = teacherService.createJwt(username, teacher);
            result.put("status", 200);
            message = "登陆成功";
            // 更新ip地址
            String ipAddress = IpUtil.getIpAddress(request);
            if ("".equals(ipAddress) || !teacherService.update(new UpdateWrapper<Teacher>().set("recent_ip", ipAddress).eq("id", teacher.getId()))) {
                message = "ip地址异常";
            }
        }
        if ((Integer) result.get("status") == 400)
            return CommonResult.failed(result.get("msg").toString());

        return CommonResult.success(result, message);
    }

    @GetMapping("login/forbidTime")
    public CommonResult<Object> loginFib(){
        return CommonResult.success(true);
    }

    @GetMapping("login/failCount")
    public CommonResult<Object> failCount(String userNumber){
        return CommonResult.success(true);
    }

    @GetMapping("login/getVerificationCode")
    public CommonResult<Object> getVerificationCode(){
        Map<String,String> map = new HashMap<>();
        map.put("verificationCodeKey", "dfjgikdfng");
        map.put("verificationCodeValue", "666666");
        return CommonResult.success(map);
    }

    @PostMapping("login/judgeType")
    public CommonResult<Object> judgeType(@RequestBody LoginUser user){
        QueryWrapper<Student> qw = new QueryWrapper<>();
        QueryWrapper<Teacher> qwt = new QueryWrapper<>();
        qw.eq("student_number",user.getUsername());
        qwt.eq("teacher_number",user.getUsername());
        Student one = studentService.getOne(qw);
        Teacher one1 = teacherService.getOne(qwt);
        if (one == null && one1 == null){
            return CommonResult.failed("用户不存在");
        } else {
            JudgeType type = new JudgeType();
            type.setIsStudent(one != null);
            return CommonResult.success(type);
        }
    }

    @GetMapping("test")
    public String test(){
        System.out.println(studentService.list());
        return "200";
    }
}
