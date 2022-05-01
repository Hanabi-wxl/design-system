package edu.dlu.bysj.log.aop;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Log;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.util.HttpContextUtil;
import edu.dlu.bysj.base.util.IpUtil;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.TeacherService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.log.enums.LogType;
import edu.dlu.bysj.log.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/23 22:13
 */
@Aspect
@Component
public class LogAspect implements Ordered {
    @Autowired
    private LogService logService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    public static final String TEACHER_LOGIN = "/login/teacher";
    public static final String STUDENT_LOGIN = "/login/student";

    @Pointcut("@annotation(edu.dlu.bysj.log.annotation.LogAnnotation)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        System.out.println("方法开始前执行");
        Object result = point.proceed();
        this.normalLog(point);
        System.out.println("方法执行结束"); 
        return result;
    }

    @AfterThrowing(value = "logPointCut()", throwing = "ex")
    public void error(JoinPoint joinPoint, Throwable ex) {
        this.errorLog(joinPoint);
    }

    /**
     * 正常日志
     */
    private boolean normalLog(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logInfo = method.getAnnotation(LogAnnotation.class);
        HttpServletRequest httpServletRequest = HttpContextUtil.getHttpServletRequest();
        Log log = null;
        if (ObjectUtil.isNotNull(httpServletRequest)) {
            log = packageParam(httpServletRequest, logInfo.content(), logInfo.type().getCode());
        }
        return logService.save(log);
    }

    /**
     * 参数打包
     */
    private Log packageParam(HttpServletRequest request, String content, Integer typeCode) {
        String jwt = request.getHeader("jwt");
        Log log = new Log();
        if (!StringUtils.isEmpty(jwt)) {
            log.setUserId(JwtUtil.getUserId(jwt));
            log.setIsStudent(Integer.valueOf(JwtUtil.getUserType(jwt)));
        } else {
            // 从request 中获取参数;
            String username = request.getParameter("username");
            // 获取请求地址;
            String requestURI = request.getRequestURI();
            if (requestURI.contains(STUDENT_LOGIN)) {
                // 学生登录
                Student student = studentService.studentPassword(username);
                if (ObjectUtil.isNotNull(student)) {
                    log.setUserId(student.getId());
                    log.setIsStudent(0);
                }
            }

            if (requestURI.contains(TEACHER_LOGIN)) {
                Teacher teacher =
                        teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_number", username));
                if (ObjectUtil.isNotNull(teacher)) {
                    log.setUserId(teacher.getId());
                    log.setIsStudent(1);
                }
            }
        }
        log.setDate(LocalDateTime.now());
        log.setType(typeCode);
        log.setIp(IpUtil.getIpAddress(request));
        log.setMessage(content);
        return log;
    }

    /**
     * 错误日志
     */
    private boolean errorLog(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logInfo = method.getAnnotation(LogAnnotation.class);
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        Log log = null;
        if (ObjectUtil.isNotNull(request)) {
            log = this.packageParam(request, logInfo.content(), LogType.ERROR_TYPE.getCode());
        }
        return logService.save(log);
    }

    /**
     * aop执行顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}
