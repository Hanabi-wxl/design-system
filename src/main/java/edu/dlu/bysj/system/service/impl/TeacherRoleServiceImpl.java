package edu.dlu.bysj.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dlu.bysj.base.model.entity.TeacherRole;
import edu.dlu.bysj.base.model.vo.AdminVo;
import edu.dlu.bysj.system.mapper.TeacherRoleMapper;
import edu.dlu.bysj.system.service.TeacherRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/17 11:41
 */
@Service
public class TeacherRoleServiceImpl extends ServiceImpl<TeacherRoleMapper, TeacherRole> implements TeacherRoleService {
    @Autowired
    private TeacherRoleMapper teacherRoleMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Set<Integer> teacherRoleName(String teacherId) {
        String key = this.getClass().getName() + ":teacherRoleName:" + teacherId;
        Set<Integer> result = null;
        if (redisTemplate.hasKey(key)) {
            result = redisTemplate.opsForSet().members(key);
        } else {
            result = teacherRoleMapper.getAllByRoleId(teacherId);
            for (Integer element : result) {
                redisTemplate.opsForSet().add(key, element);
            }
            redisTemplate.expire(key, 10, TimeUnit.DAYS);
        }
        return result;
    }

    @Override
    public List<AdminVo> teacherMangerRoles(Integer roleId, Integer majorId) {
        String key = this.getClass().getName() + ":teacherMangerRoles:" + roleId;
        List<AdminVo> adminVos = null;
        if (redisTemplate.hasKey(key)) {
            // 范围为0 -1返回所有值;
            adminVos = redisTemplate.opsForList().range(key, 0, -1);
        } else {
            adminVos = teacherRoleMapper.getMangerList(roleId, majorId);
            //存入redis;
//            redisTemplate.opsForList().rightPushAll(key, adminVos == null ? "" : adminVos);
//            redisTemplate.expire(key, 10, TimeUnit.DAYS);
        }
        return adminVos;
    }
}
