package edu.dlu.bysj.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Topics;
import edu.dlu.bysj.base.model.vo.*;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/31 14:16
 */

public interface TopicService extends IService<Topics> {
    /**
     * 通过studentId 获取该学生的志愿情况
     *
     * @param studentId 学生id
     * @return 已选题目集合
     */
    List<SelectedVo> studentSelectSubject(Integer studentId);

    /**
     * 通过教师id, 年级查询该年度下该老师所报的题目被学生的选择情况
     *
     * @param teacherId  教师id
     * @param grade      年级
     * @param pageNUmber 当前页码
     * @param pageSize   每页记录数
     * @return TotalPackageVo < TotalVolunteerPackage<UnselectStudentVo> > 结构的返回值
     */
    TotalPackageVo<TotalVolunteerPackage<UnselectStudentVo>> unselectStudentList(Integer teacherId, Integer grade, Integer pageNUmber, Integer pageSize);


    /**
     * 模糊查询未选题学生信息
     *
     * @param studentNumber 学号
     * @param studentName   学生姓名
     * @param year          年级
     * @param majorId       专业
     * @return
     */
    List<UnselectStudentVo> unChooseStudentList(String studentNumber, String studentName, Integer year, Integer majorId);

    /**
     * 获取未被选择的题目列表
     *
     * @param teacherNumber 教工号
     * @param teacherName   教师名称
     * @param year          年级
     * @param majorId       专业id
     * @return
     */
    List<UnSelectTopicVo> unChooseSubjectList(String teacherNumber, String teacherName, Integer year, Integer majorId);
}
