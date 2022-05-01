package edu.dlu.bysj.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dlu.bysj.base.model.dto.DegreeAndTileConvey;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.enums.SexEnum;
import edu.dlu.bysj.base.model.enums.TitleRankEnum;
import edu.dlu.bysj.base.model.query.UserListQuery;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.base.util.SimpleHashUtil;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import edu.dlu.bysj.common.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 教工信息表 服务实现类
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
        implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Integer> getTeacherRoles(Integer teacherId) {
        List<Integer> integers = teacherMapper.teacherRoles(teacherId);
        Integer integer = integers.get(0);
        while (integer-- > 2){
            integers.add(integer);
        }
        return integers;
    }

    @Override
    public TotalPackageVo<UserVo> getTeacherByUserQuery(UserListQuery query) {

        // 当前总页数
        if (query.getPageNumber() > 0) {
            query.setPageNumber((query.getPageNumber() - 1) * query.getPageSize());
        }
        // 总记录数目
        Integer total = teacherMapper.totalTeacherLimitByQuery(query);
        // 分页查询
        List<UserVo> userVos = teacherMapper.teacherLimitByQuery(query);

        TotalPackageVo<UserVo> packageVo = new TotalPackageVo<>();
        packageVo.setTotal(total);
        packageVo.setArrays(userVos);
        return packageVo;
    }

    @Override
    public Map<String, Object> teacherHeadDetail(String teacherId) throws JsonProcessingException {
        final String key = this.getClass().getName() + ":DegreeAndTile:" + teacherId;
        DegreeAndTileConvey degreeAndTileConvey = null;

        if (redisTemplate.hasKey(key)) {
            Object res = redisTemplate.opsForValue().get(key);
            degreeAndTileConvey = objectMapper.readValue(res.toString(), DegreeAndTileConvey.class);
        } else {

            degreeAndTileConvey = teacherMapper.teacherDegreeAndTitle(teacherId);
            String value = objectMapper.writeValueAsString(degreeAndTileConvey);
            // 设置10天过期时间
            redisTemplate.opsForValue().set(key, value, 10, TimeUnit.DAYS);
        }

        Optional<DegreeAndTileConvey> degreeTitleValue = Optional.ofNullable(degreeAndTileConvey);

        Map<String, Object> result = new HashMap<>(16);
        if (degreeTitleValue.isPresent()) {
            String rank = TitleRankEnum.backDescription(degreeAndTileConvey.getTitleLevel());
            result.put("degree", degreeAndTileConvey.getDegreeName());
            result.put("title", degreeAndTileConvey.getTitleName() + "-" + rank);
            result.put("office", degreeAndTileConvey.getOfficeName());
            result.put("degreeId", degreeAndTileConvey.getDegreeId());
            result.put("titleId", degreeAndTileConvey.getTitleId());
            result.put("officeId", degreeAndTileConvey.getOfficeId());
            result.put("majorId", degreeAndTileConvey.getMajorId());
            result.put("majorName", degreeAndTileConvey.getMajorName());
        }
        return result;
    }

    @Override
    public boolean changeTeacherInformation(ModifyUserVo userVo) {
        // 性别转换;
        userVo.setSex(SexEnum.sexDescription(userVo.getSex()));
        return teacherMapper.updateTeacherInformation(userVo) == 1 ? true : false;
    }

    @Override
    public Map<String, Object> createJwt(String teacherNumber, Teacher teacher) {
        // 查询该教师所属的学校id;
        Integer schoolId = teacherMapper.schoolIdByTeacherNumber(teacherNumber);

        // 查询教师对应的角色;
        List<Integer> teacherRoles = this.getTeacherRoles(teacher.getId());
        String jwt =
                JwtUtil.generateToken(
                        teacher.getId(),
                        teacher.getName(),
                        teacher.getPassword(),
                        teacher.getTeacherNumber(),
                        teacherRoles,
                        "1",
                        teacher.getMajorId(),
                        schoolId);
        Map<String, Object> map = new HashMap<>(16);
        map.put("JWT", jwt);
        map.put("teacherName", teacher.getName());
        map.put("roleIds", teacherRoles);
        map.put("majorId", teacher.getMajorId());
        map.put("collegeId", schoolId);
        return map;
    }

    @Override
    public List<TeacherDetailVo> getTeacherInfo(Integer majorId) {
        List<TeacherDetailVo> result = null;
        String key = RedisKeyEnum.MAJOR_TEACHER_INFO_KEY.getKeyValue() + majorId;
        if (redisTemplate.hasKey(key)) {
            Long size = redisTemplate.boundListOps(key).size();
            // 获取
            result = redisTemplate.boundListOps(key).range(0, size);
        } else {
            result = teacherMapper.getTeacherDetailInformation(majorId);
            // 放入;
            if (result != null && !result.isEmpty()) {
                redisTemplate.opsForList().rightPushAll(key, result);
            }
        }
        return result;
    }

    @Override
    public List<CollegeMajorVo> teacherMajorByCollegeId(Integer majorId) {
        String key = RedisKeyEnum.COLLEGE_MAJOR_KEY.getKeyValue() + majorId;
        List<CollegeMajorVo> collegeMajorVos = null;
        if (redisTemplate.hasKey(key)) {
            Long size = redisTemplate.boundListOps(key).size();
            collegeMajorVos = redisTemplate.boundListOps(key).range(0, size);
        } else {
            collegeMajorVos = teacherMapper.getTeacherMajorByCollegeId(majorId);
            if (collegeMajorVos != null && !collegeMajorVos.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(collegeMajorVos);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }
        }
        return collegeMajorVos;
    }

    @Override
    public List<Integer> teacherAllId() {
        return teacherMapper.getAllTeacherId();
    }


    @Override
    public List<RoleSimplifyVo> teacherRoleNameAndId(List<Integer> roleId) {
        return teacherMapper.getTeacherRoleName(roleId);
    }

    @Override
    public List<TeacherSimplyVo> majorTeacherSimplfyInfo(Integer majorId) {
        String key = RedisKeyEnum.MAJOR_ALL_TEACHER_NAME_ID_KEY.getKeyValue() + majorId;
        List<TeacherSimplyVo> result = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Long size = redisTemplate.boundListOps(key).size();
            result = (List<TeacherSimplyVo>) redisTemplate.boundListOps(key).range(0, size).get(0);
        } else {
            result = teacherMapper.selectAllByMajorId(majorId);
            if (result != null && !result.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(result);
            }
        }
        return result;
    }

    @Override
    public TeacherShortenVo teacherShortenInfo(Integer teacherId) throws JsonProcessingException {
        String key = RedisKeyEnum.TEACHER_SHORTEN_INFO_KEY.getKeyValue() + teacherId;
        TeacherShortenVo result = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Object target = redisTemplate.boundValueOps(key).get();
            if (ObjectUtil.isNotNull(target)) {
                result = objectMapper.readValue(target.toString(), TeacherShortenVo.class);
            }
        } else {
            result = teacherMapper.selectTeacherInfoByTeacherId(teacherId);
            /*写入*/
            if (ObjectUtil.isNotNull(result)) {
                String value = objectMapper.writeValueAsString(result);
                redisTemplate.boundValueOps(key).set(value);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }

        }
        return result;
    }

    @Override
    public boolean addTeacher(ModifyUserVo userVo) {
        String password = SimpleHashUtil.generatePassword(userVo.getUserNumber(), userVo.getUserNumber());
        Teacher teacher = new Teacher();
        BeanUtil.copyProperties(userVo,teacher);
        teacher.setPassword(password);
        teacher.setPhoneNumber(userVo.getPhone());
        teacher.setName(userVo.getUserName());
        teacher.setTeacherNumber(userVo.getUserNumber());
        teacher.setCanUse(1);
        teacher.setStatus(1);
        int insert = teacherMapper.insert(teacher);
        return insert != 0;
    }
}
