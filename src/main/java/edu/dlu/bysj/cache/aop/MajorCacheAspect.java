package edu.dlu.bysj.cache.aop;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.util.HttpContextUtil;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import edu.dlu.bysj.system.service.MajorService;
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
import java.util.List;
import java.util.Optional;

/**
 * @author XiangXinGang
 * @date 2021/10/26 19:46
 */
@Aspect
@Component
@Slf4j
public class MajorCacheAspect implements Ordered {
    private final RedisTemplate<String, Object> redisTemplate;
    private final MajorService majorService;
    private final TeacherMapper teacherMapper;
    public static final String MAJOR_UPDATE = "/system/major/addOrChange";
    public static final String MAJOR_DELETE = "/system/major/delete/";

    @Autowired
    public MajorCacheAspect(RedisTemplate<String, Object> redisTemplate,
                            MajorService majorService,
                            TeacherMapper teacherMapper) {
        this.redisTemplate = redisTemplate;
        this.majorService = majorService;
        this.teacherMapper = teacherMapper;
    }

    /**
     * 该aop执行顺序晚于日志aop;
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 2;
    }

    @Pointcut("execution(* edu.dlu.bysj.system.controller.MajorController.majorModify(..)) || execution(* edu.dlu.bysj.system.controller.MajorController.delete(..))")
    public void majorChange() {
    }

    @Before(value = "execution(* edu.dlu.bysj.system.controller.ScheduleController.changeFunctionTime(..))")
    public void changeFunctionTimeBefore(JoinPoint joinPoint) {
        log.info("修改时间表切面方法执行");
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Integer majorId = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if ("majorId".equals(parameterNames[i])) {
                majorId = (Integer) args[i];
            }
        }

        if (ObjectUtil.isNotNull(majorId)) {
            /*删除-专业功能时间表缓存key*/
            String key = RedisKeyEnum.MAJOR_FUNCTION_TABLE_KEY.getKeyValue() + majorId;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                redisTemplate.delete(key);
            }
        }
    }


    @Before(value = "majorChange()")
    public void majorModifyAndDelete(JoinPoint joinPoint) {
        log.info("专业修改删除专业zsetKey, hashKey");
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String requestUri = request.getRequestURI();
        Integer collegeId = null;
        Integer majorId = null;
        if (requestUri != null && requestUri.contains(MAJOR_UPDATE)) {
            Major major = (Major) args[0];
            if (major != null && ObjectUtil.isNotNull(major.getCollegeId())) {
                collegeId = major.getCollegeId();
                majorId = major.getId();
            }
        } else if (requestUri != null && requestUri.contains(MAJOR_DELETE)) {
            majorId = (Integer) args[0];
            /*当删除major时，删除其下的teacher-name-id key*/
            this.deleteMajorTeacherNameAndId(majorId);

            /*根据majorId获取学院并删除对应学院的缓存 hashKey ,zsetKey*/
            Major byId = majorService.getById(majorId);
            Optional<Major> value = Optional.ofNullable(byId);
            if (value.isPresent()) {
                if (ObjectUtil.isNotNull(value.get().getCollegeId())) {
                    collegeId = value.get().getCollegeId();
                }
            }
        }
        log.info("collegeId = {}", collegeId);
        this.deleteCollegeMajorPagination(collegeId);
        this.deleteCollegeMajorInfo(collegeId);
        /*改变major则删除该major下的teacher-shorten缓存*/
        this.deleteTeacherShortInfo(majorId);

    }

    /**
     * 根据collegeId ,删除redis中对应的学院专业 hashKey, zsetKey;
     *
     * @param collegeId
     */
    private void deleteCollegeMajorPagination(Integer collegeId) {
        if (ObjectUtil.isNotNull(collegeId)) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.MAJOR_ZSET_KEY.getKeyValue() + collegeId))) {
                redisTemplate.delete(RedisKeyEnum.MAJOR_ZSET_KEY.getKeyValue() + collegeId);
            }

            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.MAJOR_HASH_KEY.getKeyValue() + collegeId))) {
                redisTemplate.delete(RedisKeyEnum.MAJOR_HASH_KEY.getKeyValue() + collegeId);
            }
        }
    }

    private void deleteMajorTeacherNameAndId(Integer majorId) {
        log.info("删除专业下的teacher name,id");
        log.info("majorId = {}", majorId);
        if (ObjectUtil.isNotNull(majorId)) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.MAJOR_ALL_TEACHER_NAME_ID_KEY.getKeyValue() + majorId))) {
                redisTemplate.delete(RedisKeyEnum.MAJOR_ALL_TEACHER_NAME_ID_KEY.getKeyValue() + majorId);
            }
        }
    }

    /**
     * 循环删除major 下的所有 TEACHER_SHORTEN_INFO_KEY+teacherId;
     *
     * @param majorId 专业id;
     */
    private void deleteTeacherShortInfo(Integer majorId) {
        if (ObjectUtil.isNotNull(majorId)) {
            List<Integer> list = teacherMapper.selectAllTeacherIdByMajorId(majorId);
            if (list == null || list.isEmpty()) {
                return;
            }

            for (Integer element : list) {
                if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.TEACHER_SHORTEN_INFO_KEY.getKeyValue() + element))) {
                    redisTemplate.delete(RedisKeyEnum.TEACHER_SHORTEN_INFO_KEY.getKeyValue() + element);
                }
            }
        }
    }

    /**
     * 删除学院下的 major-name,id 缓存
     *
     * @param collegeId
     */
    private void deleteCollegeMajorInfo(Integer collegeId) {
        if (ObjectUtil.isNotNull(collegeId)) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.COLLEGE_MAJOR_SIMPLIFY_INFO_KEY.getKeyValue() + collegeId))) {
                redisTemplate.delete(RedisKeyEnum.COLLEGE_MAJOR_SIMPLIFY_INFO_KEY.getKeyValue() + collegeId);
            }
        }
    }
}
