package edu.dlu.bysj.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.query.*;
import edu.dlu.bysj.base.model.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/10/24 16:43
 */
public interface SubjectService extends IService<Subject> {

    /**
     * 根据学生id查询学生题目简易信息
     *
     * @param studentId 学生id
     * @return 简易学生题目对象
     */
    SubjectSimplifyVo studentSimplifyInfo(Integer studentId);

    /**
     * 通过查询对象查询题目信息;
     *
     * @param query 查询对象
     * @return approvalTaskBook 包装后的返回值对象
     */
    TotalPackageVo<ApprovalTaskBookVo> subjectApprovalInfo(TopicApprovalListQuery query);


    /**
     * 通过id 获取subjectInfo
     *
     * @param subjectId 题目id
     * @return 题目信息集合;
     */
    SubjectDetailInfoVo obtainsSubjectRelationInfo(String subjectId);

    /**
     * 通过query 查询教师自带的题目列表
     *
     * @param query     查询集合
     * @param teacherId 教师id
     * @return 查询分页结果集
     */
    TotalPackageVo<SubjectDetailVo> teacherSubjectList(SubjectListQuery query, Integer teacherId);

    /**
     * 根据条件分页查询题目审批列表;
     *
     * @param query 查询条件
     * @return 使用totalPackVo 包装后的返回结果
     */
    TotalPackageVo<ApproveDetailVo> administratorApproveSubjectPagination(SubjectApproveListQuery query);


    /**
     * 通过学生，学号, 姓名 查询管理员所能看到的题目审批列表;
     *
     * @param query 查询条件
     * @return 使用totalPackageVo 封装后的结果集;
     */
    TotalPackageVo<ApproveDetailVo> adminApprovePaginationByStudentCondition(ApproveConditionQuery query);

    /**
     * 通过教工号，教师名称，查询管理所能看到的题目审批表
     *
     * @param query 查询条件
     * @return 使用totalPackageVo 封装后的结果集;
     */
    TotalPackageVo<ApproveDetailVo> adminApprovePaginationByTeacherCondition(ApproveConditionQuery query);


    /**
     * 根据查询条件获取学生选题时查看的列表
     *
     * @param topicsListQuery 查询条件
     * @param majorId         专业id
     * @param grade           年级
     * @return TotalPackageVo<TopicsVo>封装后的结果集合;
     */
    TotalPackageVo<TopicsVo> studentSelectSubjectInfo(TopicsListQuery topicsListQuery, Integer majorId, Integer grade);


    /**
     * 通过teacherId 查询该grade 下它所确定的题目列表(以选完成的)
     *
     * @param grade      年级
     * @param teacherId  教师id
     * @param pageNumber 当前开始位置
     * @param pageSize   每页记录数
     * @return totalPackage包装后的determineStudentVo List;
     */
    TotalPackageVo<DetermineStudentVo> studentEnsureSubjectByTeacherId(Integer grade, Integer teacherId, Integer pageNumber, Integer pageSize);


    /**
     * 插入题目审批表
     *
     * @param subjectApprovalVo 插入信息
     * @return
     */
    boolean addedApprove(SubjectApprovalVo subjectApprovalVo, Integer majorId);


    /**
     * 修改题目审批表
     *
     * @param subjectApprovalVo
     * @return
     */
    boolean modifyApprove(SubjectApprovalVo subjectApprovalVo);

    boolean removeSubjectById(String subjectId);

    List<Subject> listBySubjectIds(String id1, String id2);

    /*
     * @Description: 专业管理员获取本专业所有课题
     * @Author: sinre
     * @Date: 2022/5/4 13:48
     * @param query
     * @param majorId
     * @return edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.SubjectDetailVo>
     **/
    TotalPackageVo<SubjectDetailVo> majorAdminSubjectList(SubjectListQuery query, Integer majorId);

    Subject getBySubjectId(String subjectId);

    List<Subject> getBySubjectIds(String[] subjectIds);

    /*
     * @Description: 学生获取个人报题列表
     * @Author: sinre
     * @Date: 2022/5/5 23:01
     * @param userId
     * @return edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.SubjectDetailVo>
     **/
    TotalPackageVo<SubjectDetailVo> studentSubjectList(SubjectListQuery query, Integer userId);

    /*
     * @Description: 院管理员获取本院所有课题
     * @Author: sinre
     * @Date: 2022/5/7 22:45
     * @param query
     * @return edu.dlu.bysj.base.model.vo.TotalPackageVo<edu.dlu.bysj.base.model.vo.ApproveDetailVo>
     **/
    TotalPackageVo<ApproveDetailVo> collegeAdminiApproveSubjectPagination(SubjectApproveListQuery query);

    /*
     * @Description: 根据id查询课题
     * @Author: sinre
     * @Date: 2022/5/8 14:21
     * @param firstSubjectId
     * @param secondSubjectId
     * @return java.util.List<edu.dlu.bysj.base.model.entity.Subject>
     **/
    List<Subject> listSubjectByIds(String firstSubjectId, String secondSubjectId);

    Map<String,String> obtainsSubjectInfoById(Integer subjectId);

}
