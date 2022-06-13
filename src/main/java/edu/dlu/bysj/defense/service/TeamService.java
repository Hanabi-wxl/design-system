package edu.dlu.bysj.defense.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Team;
import edu.dlu.bysj.base.model.vo.ReplyTeacherVo;
import edu.dlu.bysj.base.model.vo.StudentGroupVo;
import edu.dlu.bysj.base.model.vo.TeacherSimplyVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/7 9:44
 */
public interface TeamService extends IService<Team> {

    /**
     * 通过majorId查询本学院其他专业的教师信息
     *
     * @param majorId 专业id
     * @return
     */
    List<TeacherSimplyVo> otherMajorTeacherInfo(Integer majorId);


    /**
     * 根据教师id,和年份确定该教师的答辩组信息
     *
     * @param teacherId 教师id
     * @param grade     年级
     * @param isStudent 是否是学生
     * @return 教师信息
     */
    List<ReplyTeacherVo> teacherReplyInfo(Integer teacherId, Integer grade, Integer isStudent);

    /**
     * 获取与该教师同组的同年级的学生信息
     *
     * @param majorId  教师的专业id
     * @param grade    年级
     * @param isSecond 是否为学生
     * @return 学生分组信息集合
     */
    List<StudentGroupVo> studentReplyInfoOfSimilarGroup(Integer majorId, Integer grade, Integer isSecond, Integer teacherId);
}
