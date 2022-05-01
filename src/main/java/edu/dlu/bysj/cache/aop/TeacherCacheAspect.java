package edu.dlu.bysj.cache.aop;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.ModifyUserVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author XiangXinGang
 * @date 2021/10/26 16:43
 */
@Aspect
@Component
@Slf4j
public class TeacherCacheAspect implements Ordered {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 该aop 执行顺序小于小于日志aop;
     */
    @Override
    public int getOrder() {
        return 2;
    }

    @Before(
            value =
                    "execution(* edu.dlu.bysj.system.controller.UserController.modifyUserInformation(..))")
    public void changeTeacherInformationBefore(JoinPoint joinPoint) {
        log.info("修改教师信息方法执行aop");

        // 删除所有和修改教师权限有管的redis-key;
        // 获取参数该方法只有一个对象UserVO
        Object[] args = joinPoint.getArgs();
        ModifyUserVo arg = (ModifyUserVo) args[0];
        // 对key 进行判断并删除;
        Optional<ModifyUserVo> value = Optional.ofNullable(arg);

        if (value.isPresent()) {
            /*删除-专业教师信息进行缓存*/
            if (ObjectUtil.isNotNull(arg.getMajorId())) {
                if (Boolean.TRUE.equals(
                        redisTemplate.hasKey(
                                RedisKeyEnum.MAJOR_TEACHER_INFO_KEY.getKeyValue() + value.get().getMajorId()))) {
                    redisTemplate.delete(
                            RedisKeyEnum.MAJOR_TEACHER_INFO_KEY.getKeyValue() + value.get().getMajorId());
                }
            }
            /*删除-教师学位和职称缓存Key*/
            if (ObjectUtil.isNotNull(arg.getUserId())) {
                if (Boolean.TRUE.equals(
                        redisTemplate.hasKey(
                                RedisKeyEnum.TEACHER_TITLE_DEGREE_KEY.getKeyValue() + value.get().getUserId()))) {
                    redisTemplate.delete(
                            RedisKeyEnum.TEACHER_TITLE_DEGREE_KEY.getKeyValue() + value.get().getUserId());
                }
            }
            /*删除专业-下的教师-name-id*/
            this.deleteMajorTeacherNameAndId(value.get().getMajorId());
            this.deleteTeacherShortenInfo(value.get().getUserId());
        }
    }


    private void deleteMajorTeacherNameAndId(Integer majorId) {
        if (ObjectUtil.isNotNull(majorId)) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.MAJOR_ALL_TEACHER_NAME_ID_KEY.getKeyValue() + majorId))) {
                redisTemplate.delete(RedisKeyEnum.MAJOR_ALL_TEACHER_NAME_ID_KEY.getKeyValue() + majorId);
            }
        }
    }

    /**
     * 删除由于该用户职称变化所影响到的 teacherId
     *
     * @param teacherId teacherid
     */
    private void deleteTeacherShortenInfo(Integer teacherId) {
        if (ObjectUtil.isNotNull(teacherId)) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyEnum.TEACHER_SHORTEN_INFO_KEY.getKeyValue() + teacherId))) {
                redisTemplate.delete(RedisKeyEnum.TEACHER_SHORTEN_INFO_KEY.getKeyValue() + teacherId);
            }
        }
    }
}
