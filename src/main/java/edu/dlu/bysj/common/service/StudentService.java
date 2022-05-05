package edu.dlu.bysj.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.query.UserListQuery;
import edu.dlu.bysj.base.model.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 学生信息表 服务类
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
public interface StudentService extends IService<Student> {
    /**
     * 根据用户userNumber 获取password;
     *
     * @param userNumber 用户名
     * @return 密码
     */
    Student studentPassword(String userNumber);

    /**
     * 根据学生major_id, name, student_number 查询学生列表
     */
    TotalPackageVo<UserVo> getStudentByUserQuery(UserListQuery query);

    ;

    /**
     * 修改学生信息
     *
     * @param userVo 修改信息集合
     * @return 操作是否成功
     */
    boolean changeStudentInformation(ModifyUserVo userVo);

    /**
     * 生产学生相关jwt;
     *
     * @param studentNumber 学号
     * @return 返回值参数封装集合;
     */
    Map<String, Object> createJwt(String studentNumber, Student student);

    /**
     * 通过学生id查看学生详细信息
     *
     * @param userId 学生id
     * @return 学生详情对象
     */
    StudentDetailVo checkStudentInfo(Integer userId);

    /**
     * 查看本专业的可调整的指导教师的题目信息
     *
     * @param majorId   专业id
     * @param year      年级
     * @param type      类型
     * @param userName  用户名
     * @param useNumber 学号\教工号
     * @return
     */
    List<StudentInfoVo> checkAdjustedSubjectMentor(Integer majorId, Integer year, Integer type, String userName, String useNumber);

    boolean addStudent(ModifyUserVo userVo);

    /*
     * @Description: 根据学号获取学生信息
     * @Author: sinre
     * @Date: 2022/5/3 21:04
     * @param number
     * @return edu.dlu.bysj.base.model.vo.StudentDetailVo
     **/
    StudentDetailVo checkStudentInfoByNumber(Integer number);

    Integer numberToId(Integer studentNumber);
}
