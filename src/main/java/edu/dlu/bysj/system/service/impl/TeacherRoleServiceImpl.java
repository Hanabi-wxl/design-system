package edu.dlu.bysj.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dlu.bysj.base.model.entity.TeacherRole;
import edu.dlu.bysj.base.model.vo.AdminVo;
import edu.dlu.bysj.base.model.vo.MajorVo;
import edu.dlu.bysj.base.model.vo.TeacherSimplyVo;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import edu.dlu.bysj.system.mapper.MajorMapper;
import edu.dlu.bysj.system.mapper.TeacherRoleMapper;
import edu.dlu.bysj.system.service.TeacherRoleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private MajorMapper majorMapper;
    @Autowired
    private TeacherMapper teacherMapper;
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
            result = teacherRoleMapper.getAllRoleById(teacherId);
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

    @Override
    public List<AdminVo> getMajorAdmin(Integer collegeId) {
        String key = this.getClass().getName() + ":getMajorAdmin:" + collegeId;
        List<AdminVo> adminVos = null;
        if(redisTemplate.hasKey(key)){
            adminVos = (List<AdminVo>) redisTemplate.opsForSet().members(key);
        } else {
            List<MajorVo> majorVos = majorMapper.selectMajorList(collegeId);
            List<Integer> majorIds = new LinkedList<>();
            List<Integer> ids1 = new LinkedList<>();
            for (MajorVo majorVo : majorVos) {
                majorIds.add(majorVo.getMajorId());
            }
            List<TeacherSimplyVo> teacherSimplyVos = teacherMapper.pluralMajorTeacherInfoByMajorId(majorIds);
            for (TeacherSimplyVo teacherSimplyVo : teacherSimplyVos) {
                // 专业教师id
                ids1.add(teacherSimplyVo.getTeacherId());
            }
            // 所有专业管理员教师id
            List<Integer> ids2 = teacherRoleMapper.getAllIdByRole(3);
            // 取本专业管理员教师id
            List<Integer> majorAminIds = (List<Integer>) CollectionUtil.intersection(ids1, ids2);
            adminVos = teacherRoleMapper.getMajorAdminList(majorAminIds);
            redisTemplate.opsForSet().add(key,adminVos);
        }
        return adminVos;
    }

    @Override
    public boolean saveRole(TeacherRole teacherRole) {
        int flag = baseMapper.insert(teacherRole);
        return removeRedisRoleCache(teacherRole, flag);
    }

    @Override
    public boolean removeRole(TeacherRole teacherRole) {
        int flag = baseMapper.delete(new QueryWrapper<TeacherRole>()
                .eq("teacher_id", teacherRole.getTeacherId())
                .eq("role_id", teacherRole.getRoleId()));
        return removeRedisRoleCache(teacherRole, flag);
    }

    private boolean removeRedisRoleCache(TeacherRole teacherRole, int flag) {
        String key1 = this.getClass().getName() + ":getAllAdmin";
        String key2 = this.getClass().getName() + ":teacherRoleName:" + teacherRole.getTeacherId();
        String key3 = this.getClass().getName() + ":getMajorAdmin:*";
        redisTemplate.delete(key1);
        redisTemplate.delete(key2);
        redisTemplate.delete(key3);
        return flag==1;
    }

    @Override
    public List<AdminVo> getAllAdminByRoleId(Integer roleId) {
        List<AdminVo> adminList = null;
        String key = this.getClass().getName() + ":teacherRoleName:" + roleId;
        if(redisTemplate.hasKey(key)){
            redisTemplate.opsForSet().members(key);
        } else {
            List<Integer> ids = teacherRoleMapper.getAllIdByRole(roleId);
            adminList = teacherRoleMapper.getMajorAdminList(ids);
            redisTemplate.opsForSet().add(key,adminList);
        }
        return adminList;
    }

    @Override
    public Map<String, Object> getAllAdmin() {
        String key = this.getClass().getName() + ":getAllAdmin";
        Map<String, Object> res = new HashMap<>();
        if (redisTemplate.hasKey(key)){
            res = (Map<String, Object>) redisTemplate.opsForValue().get(key);
        } else {
            String key1 = "majorManager";
            String key2 = "collegeManager";
            String key3 = "schoolManager";
            res.put(key1,teacherRoleMapper.getMajorAdminList(teacherRoleMapper.getAllIdByRole(3)));
            res.put(key2,teacherRoleMapper.getMajorAdminList(teacherRoleMapper.getAllIdByRole(4)));
            res.put(key3,teacherRoleMapper.getMajorAdminList(teacherRoleMapper.getAllIdByRole(5)));
            redisTemplate.opsForValue().set(key,res);
        }
        return res;
    }


}
