package edu.dlu.bysj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.dto.ApproveConditionConvey;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.query.UserListQuery;
import edu.dlu.bysj.base.model.vo.ModifyUserVo;
import edu.dlu.bysj.base.model.vo.StudentDetailVo;
import edu.dlu.bysj.base.model.vo.StudentInfoVo;
import edu.dlu.bysj.base.model.vo.UserVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学生信息表 Mapper 接口
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Repository
public interface StudentMapper extends BaseMapper<Student> {
    /**
     * 根据用户查询条件查询学生列表
     *
     * @param query 查询条件
     * @return 学生列表
     */
    List<UserVo> studentLimitByQuery(UserListQuery query);

    /**
     * 根据用户条件查询学生列表 的总数;
     *
     * @param query 查询条件
     * @return 总记录数
     */
    Integer totalStudentLimitByQuery(UserListQuery query);

    /**
     * 根据学生id 修改学生信息
     *
     * @param userVo
     * @return
     */
    int updateStudentInformation(ModifyUserVo userVo);

    /**
     * 通过学号获取所属学校的id;
     *
     * @param studentNumber 学号
     * @return 学号id
     */
    Integer getSchoolIdByStudentNumber(String studentNumber);

    /**
     * 通过学生id查询学生的详细信息
     *
     * @param userId 学生id
     * @return 学生的信息返回值;
     */
    StudentDetailVo searcherInformationById(Integer userId);

    /**
     * 通过学生的id获取学生的信息
     *
     * @param studentId 学生id
     * @return 单条 Map<Integer, Map<Integer,Object>>
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> studentInfoById(Integer studentId);


    /**
     * 查询教师自带题目列表的学生信息
     *
     * @param studentIds 学生id集合
     * @return 学生信息
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> teacherSubjectListStudentInfo(List<Integer> studentIds);


    /**
     * 通过studentId  查询student 的信息;
     *
     * @param studentIds 学生ids
     * @return 学生信息map集合
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> adminApproveStudentInfoById(List<Integer> studentIds);


    /**
     * 通过学生的name, number 条件查询该专业下的题目审批表中的记录
     *
     * @param majorId       专业id
     * @param grade         年级
     * @param start         开始位置
     * @param pageSize
     * @param studentName   学生姓名
     * @param studentNumber 学号
     * @return List<ApproveDetailVo> 集合;
     */
    List<ApproveConditionConvey> adminApproveByCondition(@Param("majorId") Integer majorId,
                                                         @Param("grade") Integer grade,
                                                         @Param("start") Integer start,
                                                         @Param("pageSize") Integer pageSize,
                                                         @Param("studentName") String studentName,
                                                         @Param("studentNumber") String studentNumber);

    /**
     * 查询 adminApproveByCondition 条件下的总条数
     *
     * @param majorId       专业id
     * @param grade         年级
     * @param studentName   学生名称
     * @param studentNumber 学号
     * @return 总条数
     */
    Integer totalAdminApproveByCondition(@Param("majorId") Integer majorId,
                                         @Param("grade") Integer grade,
                                         @Param("studentName") String studentName,
                                         @Param("studentNumber") String studentNumber);


    /**
     * 查询该专业下以确定的学生题目信息,
     *
     * @param majorId    专业id
     * @param grade      年级
     * @param type       类型
     * @param userName   用户名
     * @param userNumber 用户的学号/工号
     * @return
     */
    List<StudentInfoVo> definiteSubjectStudentList(@Param("majorId") Integer majorId,
                                                   @Param("grade") Integer grade,
                                                   @Param("type") Integer type,
                                                   @Param("userName") String userName,
                                                   @Param("userNumber") String userNumber);
}
