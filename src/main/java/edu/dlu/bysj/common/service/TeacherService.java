package edu.dlu.bysj.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.model.query.UserListQuery;
import edu.dlu.bysj.base.model.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 教工信息表 服务类
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
public interface TeacherService extends IService<Teacher> {
    /**
     * 根据教师id 获取教师角色列表
     *
     * @param teacherId 教师id
     * @return 返回教师角色集合
     */
    List<Integer> getTeacherRoles(Integer teacherId);

    /**
     * 根据query 获取教师信息
     *
     * @param query 查询参数集合
     * @return 查询结果集合
     */
    TotalPackageVo<UserVo> getTeacherByUserQuery(UserListQuery query);

    /**
     * 根据id 获取教师职称，和学位
     *
     * @param teacherId 教师id
     * @return 学位职称map;
     */
    Map<String, Object> teacherHeadDetail(String teacherId) throws JsonProcessingException;

    /**
     * 根据uservo 修改教师信息
     *
     * @param userVo 修改信息类
     * @return 操作是否成功
     */
    boolean changeTeacherInformation(ModifyUserVo userVo);

    /**
     * 根据teacherNumber 拼装jwt
     *
     * @param teacherNumber 教工号
     * @return 返回值封装集合
     */
    Map<String, Object> createJwt(String teacherNumber, Teacher teacher);

    /**
     * 通过majorId 获取教师详细信息;
     *
     * @param majorId 专业id
     * @return 教师详情列表
     */
    List<TeacherDetailVo> getTeacherInfo(Integer majorId);

    /**
     * 通过专业id查询该专业所属学院的所有专业
     *
     * @param majorId: 专业id
     * @return 学院专业集合类对象
     */
    List<CollegeMajorVo> teacherMajorByCollegeId(Integer majorId);

    /**
     * 查询教师所有的id
     *
     * @return 教师id 集合;
     */
    List<Integer> teacherAllId();

    /**
     * 通过角色id, 查询角色的name,id
     *
     * @param roleId
     * @return
     */
    List<RoleSimplifyVo> teacherRoleNameAndId(List<Integer> roleId);

    /**
     * 通过majrId 查询该专业的所有teacherId;
     *
     * @param majorId 专业id;
     * @return 该专业教师简单信息集合;
     */
    List<TeacherSimplyVo> majorTeacherSimplfyInfo(Integer majorId);

    /**
     * 通过teacherId 查询该教师的简单信息
     *
     * @param teacherId 教师id
     * @return 简单信息集合
     */
    TeacherShortenVo teacherShortenInfo(Integer teacherId) throws JsonProcessingException;

    /*
     * @Description: 添加教师
     * @Author: sinre
     * @Date: 2022/4/30 19:26
     * @param userVo
     * @return boolean
     **/
    boolean addTeacher(ModifyUserVo userVo);

    String idToNumber(Integer userId);
}
