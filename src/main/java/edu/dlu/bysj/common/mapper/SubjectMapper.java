package edu.dlu.bysj.common.mapper;

import java.util.List;

import edu.dlu.bysj.document.entity.SelectStaticsTemplate;
import edu.dlu.bysj.document.entity.SubjectSelectAnalysis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import edu.dlu.bysj.base.model.dto.AdminApprovalConvey;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.query.TopicApprovalListQuery;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.document.entity.PaperCoverTemplate;
import edu.dlu.bysj.document.entity.ReportStaticsTemplate;

/**
 * @author XiangXinGang
 * @date 2021/10/24 16:42
 */
@Repository
public interface SubjectMapper extends BaseMapper<Subject> {
    /**
     * 根据学生id 查询题目题目简化信息
     *
     * @param studentId
     *            学生id
     * @return 题目集合;
     */
    SubjectSimplifyVo subjectSimplifyInfo(Integer studentId);

    /**
     * 通过query 条件查询 题目部分信息;
     *
     * @param query
     *            查询条件对象
     * @return 查询结果集对象;
     */
    List<ApprovalTaskBookVo> subjectListByMajorId(TopicApprovalListQuery query);

    /**
     * 通过query 查看该条件下的总对象数目;
     *
     * @param query
     *            查询条件
     * @return total;
     */
    Integer totalSubjectListByMajorId(TopicApprovalListQuery query);

    /**
     * 通过teacherId, grade 查询该教师在该年分的信息
     *
     * @param teacherId
     *            教师id
     * @param grade
     *            年份
     * @param start
     *            开始位置
     * @param pageSize
     *            每页记录数
     * @return subjectDetailVo
     */
    List<SubjectDetailVo> teacherSubjectListByTeacherIdAndGrade(@Param("teacherId") Integer teacherId,
        @Param("grade") Integer grade, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    /**
     * teacherSubjectListByTeacherIdAndGrade 的总记录条数
     *
     * @param teacherId
     *            教师id
     * @param grade
     *            年份
     * @return 总记录条数
     */
    Integer totalSubjectListByTeacher(@Param("teacherId") Integer teacherId, @Param("grade") Integer grade);

    /**
     * 通过majorId, teacherId(可有), 查询该专业报题情况;
     *
     * @param majorId
     *            专业id
     * @param teacherId
     *            教师id
     * @param start
     *            开始位置
     * @param pageSize
     *            每页记录数
     * @param grade
     *            年级
     * @return
     */
    List<AdminApprovalConvey> adminApprovalListPagination(@Param("majorId") Integer majorId,
        @Param("teacherId") Integer teacherId, @Param("start") Integer start, @Param("pageSize") Integer pageSize,
        @Param("grade") Integer grade);

    /**
     * 查 adminApprovalListPagination条件下的总页数;
     *
     * @param teacherId
     *            教师id
     * @param year
     *            年份
     * @return
     */
    Integer totalAdminCollegeApprovalList(Integer majorId, Integer teacherId, String searchContent, Integer year);

    /**
     * 查询该subjectIds(被委托人题目列表) 集合下的subject信息并封装与 EntrustInfoVo
     *
     * @param subjectIds
     *            题目ids
     * @param start
     *            开始位置
     * @param pageSize
     *            每页记录数
     * @return entrustInfoVo集合;
     */
    List<EntrustInfoVo> entrustSubjectPagination(@Param("subjectIds") List<Integer> subjectIds,
        @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    /**
     * 查询 entrustSubjectPagination,所携带的总数
     *
     * @param subjectIds
     * @return 总记录数
     */
    Integer totalEntrustSubject(@Param("subjectIds") List<Integer> subjectIds);

    /**
     * 模糊查询 获取学生选题时看见的题目列表;
     *
     * @param majorId
     *            专业id
     * @param grade
     *            当前年级(now)
     * @param start
     *            开始位置
     * @param pageSize
     *            每页记录数
     * @param subjectName
     *            题目名称
     * @param teacherName
     *            教师名称
     * @return
     */
    List<TopicsVo> studentSelectSubject(@Param("majorId") Integer majorId, @Param("grade") Integer grade,
        @Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("teacherName") String teacherName,
        @Param("subjectName") String subjectName);

    /**
     * 获取 studentSelectSubject查询下的总数
     *
     * @param majorId
     *            专业id
     * @param grade
     *            年级
     * @param teacherName
     *            教师名称
     * @param subjectName
     *            题目名称
     * @return 总数
     */
    Integer totalStudentSelectSubject(@Param("majorId") Integer majorId, @Param("grade") Integer grade,
        @Param("teacherName") String teacherName, @Param("subjectName") String subjectName);

    /**
     * 通过teacherId 查询该garde下以确定该教师为指导教师的题目
     *
     * @param teacherId
     *            教师题目
     * @param grade
     *            年级
     * @param start
     *            开始位置
     * @param pageSize
     *            每页记录数
     * @return 以确定的题目返回结果集
     */
    List<DetermineStudentVo> determinStudentSubject(@Param("teacherId") Integer teacherId,
        @Param("grade") Integer grade, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    /**
     * 查询 的总数;
     *
     * @param grade
     *            年级
     * @param teacherId
     *            教师Id
     * @return 总条数
     */
    Integer totalDeterminestudentSubject(@Param("teacherId") Integer teacherId, @Param("grade") Integer grade);

    /**
     * 搜索该专业的下载论文封皮相关消息
     * 
     * @param majorId
     * @return
     */
    List<PaperCoverTemplate> selectPaperCoverInfo(Integer majorId);

    /**
     * 查询班级选题统计表详情
     * 
     * @param majorId
     * @return
     */
    List<ReportStaticsTemplate> selectAllReportStaticsByMajorId(Integer majorId);

    boolean removeSubjectById(String subjectId);

    Subject selectBySubjectId(String subjectId);

    List<Subject> listBySubjectIds(String id1, String id2);

    List<SubjectDetailVo> adminSubjectListByMajorIdAndGrade(@Param("majorId") Integer majorId,
                @Param("grade") Integer grade, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer totalSubjectListByMajor(@Param("majorId") Integer majorId, @Param("grade") Integer grade);

    SubjectDetailVo studentSubjectListByStudentIdAndGrade(@Param("studentId") Integer studentId,
                @Param("grade") Integer grade, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer totalSubjectListByStudent(Integer userId, Integer year);

    List<AdminApprovalConvey> approvalListPagination(Integer majorId, Integer teacherId, String searchContent,
                                                     Integer start, Integer pageSize, Integer year);

    List<Subject> listSubjectByIds(String id1, String id2);

    Integer totalAdminMajorApprovalList(Integer majorId, Integer teacherId, Integer grade);

    List<Integer> getIdsByMajor(Integer majorId);

    List<SubjectDetailVo> filterByYear(List<Integer> idList, Integer start, Integer pageSize, Integer year);

    Integer totalFilterByYear(List<Integer> idList, Integer year);

    List<SelectStaticsTemplate> selectAllSelectStaticsByMajorId(Integer majorId);

    SubjectSelectAnalysis selectAnalysisCount(Integer majorId, Integer grade);

    List<ReportStaticsTemplate> selectReportByMajorId(Integer key);

}
