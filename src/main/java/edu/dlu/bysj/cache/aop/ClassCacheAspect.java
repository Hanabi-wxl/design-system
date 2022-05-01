package edu.dlu.bysj.cache.aop;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import edu.dlu.bysj.base.model.entity.Class;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.ModifyClass;
import edu.dlu.bysj.system.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author XiangXinGang
 * @date 2021/10/27 11:13
 */
@Component
@Aspect
@Slf4j
public class ClassCacheAspect implements Ordered {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ClassService classService;

    @Autowired
    public ClassCacheAspect(RedisTemplate<String, Object> redisTemplate, ClassService classService) {
        this.redisTemplate = redisTemplate;
        this.classService = classService;
    }


    /**
     * 该aop落后与 日志aop
     */
    @Override
    public int getOrder() {
        return 2;
    }

    /**
     * 修改班级信息方法before-aop
     *
     * @param joinPoint 切入点
     */
    @Before(value = "execution(* edu.dlu.bysj.system.controller.ClassController.modifyClassInfo(..))")
    public void modifyClassInfoBefore(JoinPoint joinPoint) {
        log.info("修改班级信息");
        // 获取参数
        Object[] args = joinPoint.getArgs();
        ModifyClass arg = (ModifyClass) args[0];
        Optional<ModifyClass> value = Optional.ofNullable(arg);

        if (value.isPresent()) {
            log.info("majorId = {}", value.get().getMajorId());
            /*删除-专业班级名称,id缓存key*/
            if (ObjectUtil.isNotNull(value.get().getMajorId())) {
                if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.CLASS_MAJOR_NAME_ID_KEY.getKeyValue() + value.get().getMajorId()))) {
                    redisTemplate.delete(RedisKeyEnum.CLASS_MAJOR_NAME_ID_KEY.getKeyValue() + value.get().getMajorId());
                }
            }

        }
    }


    /**
     * 删除班级before-aop
     *
     * @param joinPoint 切入点
     */
    @Before(value = "execution(* edu.dlu.bysj.system.controller.ClassController.deleteClass(..))")
    public void deleteClassBefore(JoinPoint joinPoint) {
        log.info("删除班级");
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Integer classId = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if ("classId".equals(parameterNames[i])) {
                classId = JSONUtil.parseObj(args[i]).get("classId", Integer.class);
            }
        }
        /*通过班级查询到majorId,再删除*/
        Class byId = classService.getById(classId);
        Optional<Class> value = Optional.ofNullable(byId);
        if (value.isPresent()) {
            /*删除-专业班级名称,id缓存key*/
            if (ObjectUtil.isNotNull(value.get().getMajorId())) {
                if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.CLASS_MAJOR_NAME_ID_KEY.getKeyValue() + value.get().getMajorId()))) {
                    redisTemplate.delete(RedisKeyEnum.CLASS_MAJOR_NAME_ID_KEY.getKeyValue() + value.get().getMajorId());
                }
            }
        }


    }

}
