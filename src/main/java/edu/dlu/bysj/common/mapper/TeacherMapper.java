package edu.dlu.bysj.common.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import edu.dlu.bysj.base.model.dto.AdminApprovalConvey;
import edu.dlu.bysj.base.model.dto.DegreeAndTileConvey;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.model.query.UserListQuery;
import edu.dlu.bysj.base.model.vo.*;

/**
 * @author XiangXinGang
 * @date 2021/10/14 20:25
 */
@Repository
public interface TeacherMapper extends BaseMapper<Teacher> {
    /**
     * 根据教师id 获取该教师的角色
     *
     * @param teacherId
     *            教师id
     * @return 角色集合
     */
    List<Integer> teacherRoles(Integer teacherId);

    /**
     * 根据query 查询返用户信息
     *
     * @param query
     *            查询条件
     * @return userVo 类
     */
    List<UserVo> teacherLimitByQuery(UserListQuery query);

    /**
     * 根据查询teacherLimitByQuery 方法的总条数
     *
     * @param query
     *            查询条件对象
     * @return 该条件下的总条数
     */
    Integer totalTeacherLimitByQuery(UserListQuery query);

    /**
     * 根据教师id 获取教师职称和,学位, 教师必须同时具有学位和职称信息否则是无法查询出信息的;
     *
     * @param teacherId
     *            教师id
     * @return 职称map
     */
    DegreeAndTileConvey teacherDegreeAndTitle(String teacherId);

    /**
     * 根据uservo 更改教师信息
     *
     * @param userVo
     *            修改信息类
     * @return 成功记录条数
     */
    int updateTeacherInformation(ModifyUserVo userVo);

    /**
     * 通过教工号查询教师所属的学校Id
     *
     * @param teacherNumber
     *            教工号
     * @return 学校id
     */
    Integer schoolIdByTeacherNumber(String teacherNumber);

    /**
     * 通过专业id获取本专业老师详情信息;
     *
     * @param majorId
     *            专业id
     * @return 教师详细信息集合
     */
    List<TeacherDetailVo> getTeacherDetailInformation(Integer majorId);

    /**
     * 通过学院id获取老师详情信息;
     *
     * @param collegeId
     *            学院id
     * @return 教师详细信息集合
     */
    List<TeacherDetailVo> getCollegeTeacherDetailInformation(Integer collegeId);

    /**
     * 根据教师的专业id查询教师所属学院的所有专业;
     *
     * @param majorId
     *            :专业id
     * @return 该学院的所有专业的集合;
     */
    List<CollegeMajorVo> getTeacherMajorByCollegeId(Integer majorId);

    /**
     * 查询所有的teacherId;
     *
     * @return id 集合
     */
    List<Integer> getAllTeacherId();

    /**
     * 通过teacher的角色id查询角色的name ,id
     *
     * @param roleIds
     *            角色名称ids
     * @return 简单的角色名称和id集合;
     */
    List<RoleSimplifyVo> getTeacherRoleName(List<Integer> roleIds);

    /**
     * 通过majorId 查询本专业的所有老师name,id
     *
     * @param majorId
     * @return 教师, name, id 信息集合
     */
    List<TeacherSimplyVo> selectAllByMajorId(Integer majorId);

    /**
     * 通过teacherId 查询teacher-titleName,majorName,collegeName,email
     *
     * @param teacherId
     *            教师id
     * @return 简单信息封装类
     */
    TeacherShortenVo selectTeacherInfoByTeacherId(Integer teacherId);

    /**
     * 通过 majorId 查询teacherIds
     *
     * @param majorId
     *            专业is
     * @return 教师id集合;
     */
    List<Integer> selectAllTeacherIdByMajorId(Integer majorId);

    /**
     * 通过collegeId 查询该学院下的所有teacherId;
     *
     * @param collegeId
     *            学院id
     * @return teacherId 集合
     */
    Set<Integer> selectAllTeacherIdByCollegeId(Integer collegeId);

    /**
     * Map<Integer,Map<String,Object>> 方式接收返回值
     *
     * @param firstTeacherId
     *            第一教师的id
     * @param secondTeacherId
     *            第二教师id
     * @return map<Integer, Map < String, Object>>
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> firstAndSecondTeacherInfo(@Param("firstTeacherId") Integer firstTeacherId,
        @Param("secondTeacherId") Integer secondTeacherId);

    /**
     * 通过convey 中的第一第二指导教师id,查询出第一第二指导教师的信息封装到TeacherInfoConvey中
     *
     * @param conveys
     *            教师id的封装;
     * @return 查询结果集
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> TeacherInfoByTeacherId(@Param("set") Set<Integer> conveys);

    /**
     * 通过 教师教工号，姓名查询管理员所能看见的题目审批列表
     *
     * @param majorId
     *            专业id
     * @param grade
     *            年级
     * @param start
     *            开始位置
     * @param pageSize
     *            每页记录数
     * @param teacherName
     *            教师名称
     * @param teacherNumber
     *            教工号
     * @return AdminApprovalConvey集合;
     */
    List<AdminApprovalConvey> adminApproveListByTeacherCondition(@Param("majorId") Integer majorId,
        @Param("grade") Integer grade, @Param("start") Integer start, @Param("pageSize") Integer pageSize,
        @Param("teacherName") String teacherName, @Param("teacherNumber") String teacherNumber);

    /**
     * 查询 adminApproveListByTeacherCondition 总数
     *
     * @param majorId
     *            专业id
     * @param grade
     *            年级
     * @param teacherName
     *            教师姓名
     * @param teacherNumber
     *            教工号
     * @return 总条数
     */
    Integer totalAdminApproveListTeacherCondition(@Param("majorId") Integer majorId, @Param("grade") Integer grade,
        @Param("teacherName") String teacherName, @Param("teacherNumber") String teacherNumber);

    /**
     * 查询多个专业的教师信息
     *
     * @param majorId
     *            专业id
     * @return 多个真专业的majorId
     */
    List<TeacherSimplyVo> pluralMajorTeacherInfoByMajorId(List<Integer> majorId);

    /**
     * 查询题目审批表中的教师信息;
     * 
     * @param integers
     * @return
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> ApprovelFormTeacherInfo(List<Integer> integers);

    @MapKey("id")
    Map<Integer, Map<String, Object>> AllRelativeTeacherInfo(Integer firstTeacherId, Integer secondTeacherId, Integer otherTeacherId);

}
