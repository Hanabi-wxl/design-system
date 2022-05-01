package edu.dlu.bysj.cache.aop;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.College;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.util.HttpContextUtil;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

/**
 * @author XiangXinGang
 * @date 2021/10/27 15:42
 */
@Component
@Aspect
@Slf4j
public class CollegeCacheAspect implements Ordered {
    private final RedisTemplate<String, Object> redisTemplate;
    private final TeacherMapper teacherMapper;
    public static final String COLLEGE_DELETE = "/system/college/delete/";
    public static final String COLLEGE_ADD_OR_UPDATE = "system/college/addOrChange";

    @Autowired
    public CollegeCacheAspect(RedisTemplate<String, Object> redisTemplate,
                              TeacherMapper teacherMapper) {
        this.redisTemplate = redisTemplate;
        this.teacherMapper = teacherMapper;
    }

    /**
     * 该aop 落后与 日志aop
     *
     * @return aop执行顺序
     */
    @Override
    public int getOrder() {
        return 2;
    }

    @Pointcut("execution(* edu.dlu.bysj.system.controller.CollegeController.collegeAdd(..)) || execution(* edu.dlu.bysj.system.controller.CollegeController.collegeDelete(..))")
    public void schoolChange() {
    }

    @Before(value = "schoolChange()")
    public void collegeAddAndDelete(JoinPoint joinPoint) {
        log.info("学校的添加，删除，删除学校学院缓存");
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String jwt = request.getHeader("jwt");
        //通过路径判断是哪一个方法;
        String requestUri = request.getRequestURI();
        Integer schoolId = null;
        Integer collegeId = null;
        Object[] args = joinPoint.getArgs();
        if (requestUri != null && requestUri.contains(COLLEGE_ADD_OR_UPDATE)) {
            College arg = (College) args[0];
            Optional<College> value = Optional.ofNullable(arg);
            if (value.isPresent() && ObjectUtil.isNotNull(value.get().getSchoolId())) {
                schoolId = value.get().getSchoolId();
                collegeId = value.get().getId();
            }
        } else if (requestUri != null && requestUri.contains(COLLEGE_DELETE)) {
            collegeId = (Integer) args[0];
            if (!StringUtils.isEmpty(jwt)) {
                schoolId = JwtUtil.getSchoolId(jwt);
            }
        }

        if (ObjectUtil.isNotNull(schoolId)) {
            this.deleteSchoolCollegePagination(schoolId);
            this.deleteSchoolCollegeInfo(schoolId);
        }
        /*删除由于学院变化该学院下的teacher-short-info 缓存*/
        this.deleteCollegeTeacherShortenInfo(collegeId);
        this.deleteCollegeMajorInfo(collegeId);

    }

    /**
     * 删除学校学院分页表;
     *
     * @param schoolId
     */
    private void deleteSchoolCollegePagination(Integer schoolId) {
        if (ObjectUtil.isNotNull(schoolId)) {
            log.info("schoolId = {}", schoolId);
            /*删除-学院分页zset key*/
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.COLLEGE_ZSET_KEY.getKeyValue() + schoolId))) {
                redisTemplate.delete(RedisKeyEnum.COLLEGE_ZSET_KEY.getKeyValue() + schoolId);
            }

            /*删除-学院分页hash key*/
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.COLLEGE_HASH_KEY.getKeyValue() + schoolId))) {
                redisTemplate.delete(RedisKeyEnum.COLLEGE_HASH_KEY.getKeyValue() + schoolId);
            }
        }
    }

    /**
     * 删除学院信息name,id 缓存
     */
    private void deleteSchoolCollegeInfo(Integer schoolId) {
        if (ObjectUtil.isNotNull(hashCode())) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.COLLEGE_NAME_ID_KEY.getKeyValue() + schoolId))) {
                redisTemplate.delete(RedisKeyEnum.COLLEGE_NAME_ID_KEY.getKeyValue() + schoolId);
            }
        }
    }

    /**
     * 因修改学院而删除该学院下的teacher-id缓存;
     *
     * @param collegeId
     */
    private void deleteCollegeTeacherShortenInfo(Integer collegeId) {
        if (ObjectUtil.isNotNull(collegeId)) {
            Set<Integer> integers = teacherMapper.selectAllTeacherIdByCollegeId(collegeId);
            Optional<Set<Integer>> value = Optional.ofNullable(integers);
            if (value.isPresent()) {
                for (Integer element : value.get()) {
                    if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.TEACHER_SHORTEN_INFO_KEY.getKeyValue() + element))) {
                        redisTemplate.delete(RedisKeyEnum.TEACHER_SHORTEN_INFO_KEY.getKeyValue() + element);
                    }
                }
            }
        }
    }


    /**
     * 删除学院下的major-name,major-id 缓存
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
