package edu.dlu.bysj.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.enums.SexEnum;
import edu.dlu.bysj.base.model.query.UserListQuery;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.base.util.SimpleHashUtil;
import edu.dlu.bysj.common.mapper.StudentMapper;
import edu.dlu.bysj.common.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生信息表 服务实现类
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
        implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student studentPassword(String userNumber) {
        return baseMapper.selectOne(new QueryWrapper<Student>().eq("student_number", userNumber));
    }

    @Override
    public TotalPackageVo<UserVo> getStudentByUserQuery(UserListQuery query) {

        // 当前记录位置;
        if (query.getPageNumber() > 0) {
            query.setPageNumber((query.getPageNumber() - 1) * query.getPageSize());
        }
        Integer total = studentMapper.totalStudentLimitByQuery(query);
        // 分页
        List<UserVo> userVos = studentMapper.studentLimitByQuery(query);
        TotalPackageVo<UserVo> packageVo = new TotalPackageVo<>();
        packageVo.setTotal(total);
        packageVo.setArrays(userVos);

        return packageVo;
    }

    @Override
    public boolean changeStudentInformation(ModifyUserVo userVo) {
        userVo.setSex(SexEnum.sexDescription(userVo.getSex()));
        return studentMapper.updateStudentInformation(userVo) == 1 ? true : false;
    }

    @Override
    public Map<String, Object> createJwt(String studentNumber, Student student) {
        // 查询学生所属学校;id;
        Integer schoolId = studentMapper.getSchoolIdByStudentNumber(studentNumber);
        String jwt = null;
        jwt =
                JwtUtil.generateToken(
                        student.getId(),
                        student.getName(),
                        student.getPassword(),
                        student.getStudentNumber(),
                        Arrays.asList(student.getRoleId()),
                        "0",
                        student.getMajorId(),
                        schoolId);
        Map<String, Object> map = new HashMap<>(16);
        map.put("studentName", student.getName());
        map.put("JWT", jwt);
        return map;
    }

    @Override
    public StudentDetailVo checkStudentInfo(Integer userId) {
        return studentMapper.searcherInformationById(userId);
    }

    @Override
    public List<StudentInfoVo> checkAdjustedSubjectMentor(Integer majorId, Integer year, Integer type, String userName, String useNumber) {
        return studentMapper.definiteSubjectStudentList(majorId, year, type, userName, useNumber);
    }

    @Override
    public boolean addStudent(ModifyUserVo userVo) {
        String password = SimpleHashUtil.generatePassword(userVo.getUserNumber(), userVo.getUserNumber());
        Student student = new Student();
        BeanUtil.copyProperties(userVo,student);
        student.setStudentNumber(userVo.getUserNumber());
        student.setName(userVo.getUserName());
        student.setPhoneNumber(userVo.getPhone());
        student.setPassword(password);
        student.setCanUse(1);
        student.setStatus(1);
        student.setRoleId(1);
        int insert = studentMapper.insert(student);
        return insert != 0;
    }

    @Override
    public StudentDetailVo checkStudentInfoByNumber(Integer number) {
        return studentMapper.checkStudentInfoByNumber(number);
    }

    @Override
    public Integer numberToId(Integer studentNumber) {
        return studentMapper.numberToId(studentNumber);
    }

}
