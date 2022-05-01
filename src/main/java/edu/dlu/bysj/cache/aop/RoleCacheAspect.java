package edu.dlu.bysj.cache.aop;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.util.HttpContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author XiangXinGang
 * @date 2021/10/27 21:24
 */
@Component
@Aspect
@Slf4j
public class RoleCacheAspect implements Ordered {
    /**
     * 设置该aop的执行顺序, 落后于日志aop
     *
     * @return aop 执行顺序;
     */
    @Override
    public int getOrder() {
        return 2;
    }

    public final RedisTemplate<String, Object> redisTemplate;

    public static final String ROLE_ADD = "/system/role/add";

    public static final String ROLE_DELETE = "/system/role/delete/";

    public static final String ROLE_ID = "roleId";

    public static final String TEACHER_ID = "teacherId";

    @Autowired
    public RoleCacheAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("execution(*  edu.dlu.bysj.system.controller.RoleController.roleAdd(..)) || execution(* edu.dlu.bysj.system.controller.RoleController.roleDelete(..))")
    public void roleChange() {
    }


    @Before("roleChange()")
    public void roleAddAndUpdate(JoinPoint joinPoint) {
        log.info("对角色修改删除，时删除对应的key");
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String requestUri = request.getRequestURI();
        /*删除总角色缓存, 角色-功能缓存，系统三类管理员缓存,教师-角色,总角色-角色名称*/
        this.deleteTotalRole();
        this.deleteRoleNameAndId();
        this.cycleDeleteSystemRoleName();
        this.cycleDeleteRoleFunction();
        this.cycleDeleteTeacherRoleName();
    }


    @Before("execution(* edu.dlu.bysj.system.controller.RoleController.roleFunctionModify(..))")
    public void roleFunctionModifyBefore(JoinPoint joinPoint) {
        log.info("删除角色-功能对应");
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Integer roleId = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if (ROLE_ID.equals(parameterNames[i])) {
                roleId = (Integer) args[i];
            }
        }
        log.info("roleId = {}", roleId);
        this.deleteRoleFunctionByRoleId(roleId);
    }

    @Before("execution(* edu.dlu.bysj.system.controller.UserController.modifyTeacherRole(..))")
    public void modifyTeacherRoleBefore(JoinPoint joinPoint) {
        /*教师对角色-名称*/
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Integer teacherId = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if (TEACHER_ID.equals(parameterNames[i])) {
                teacherId = (Integer) args[i];
            }
        }
        /*清除-教师对角色-名称, 系统总的三类管理员*/
        this.deleteTeacherRole(teacherId);
        this.cycleDeleteSystemRoleName();
    }


    /**
     * 删除总的角色缓存
     */
    private void deleteTotalRole() {
        log.info("总角色");
        redisTemplate.delete(RedisKeyEnum.TOTAL_ROLE_KEY.getKeyValue());
    }

    /**
     * 删除总的角色名称_id缓存
     */
    private void deleteRoleNameAndId() {
        log.info("总角色名称");
        redisTemplate.delete(RedisKeyEnum.ROLE_NAME_ID_KEY.getKeyValue());
    }

    /**
     * 删除对应角色下的功能模块缓存;
     *
     * @param roleId 角色id
     */
    private void deleteRoleFunctionByRoleId(Integer roleId) {
        if (ObjectUtil.isNotNull(roleId)) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.ROLE_FUNCTION_KEY.getKeyValue() + roleId))) {
                redisTemplate.delete(RedisKeyEnum.ROLE_FUNCTION_KEY.getKeyValue() + roleId);
            }
        }
    }

    /**
     * 删除所有的教师对角色名称的缓存
     */
    private void cycleDeleteTeacherRoleName() {
        log.info("教师对角色名称");
        /*所有teacherId*/
        /*key的模式  TEACHER_ROLE_NAME_KEY.getKeyValue() + teacherId ,可以使用模糊删除*/
        Set<String> keys = redisTemplate.keys(RedisKeyEnum.TEACHER_ROLE_NAME_KEY.getKeyValue() + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除系统三类成员缓存key;
     */
    private void cycleDeleteSystemRoleName() {
        log.info("系统三类管理员");
        /*查询所有角色id*/
        /*key的模式 : TEACHER_MANAGER_ROLE_KEY.getKeyValue() + roleId*/
        Set<String> keys = redisTemplate.keys(RedisKeyEnum.TEACHER_MANAGER_ROLE_KEY.getKeyValue() + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除所有的角色-功能缓存
     */
    private void cycleDeleteRoleFunction() {
        log.info("角色-功能");
        /*查询所有角色id*/
        /*key的模式 - RedisKeyEnum.ROLE_FUNCTION_KEY.getKeyValue()+ roleId */
        Set<String> keys = redisTemplate.keys(RedisKeyEnum.ROLE_FUNCTION_KEY.getKeyValue() + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 根据教师id 删除教师-角色缓存
     *
     * @param teacherId 教师id;
     */
    private void deleteTeacherRole(Integer teacherId) {
        log.info("删除对应教师的角色缓存");
        if (ObjectUtil.isNotNull(teacherId)) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.TEACHER_ROLE_NAME_KEY.getKeyValue() + teacherId))) {
                redisTemplate.delete(RedisKeyEnum.TEACHER_ROLE_NAME_KEY.getKeyValue() + teacherId);
            }
        }

    }


}
