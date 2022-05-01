package edu.dlu.bysj.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Topics;
import edu.dlu.bysj.base.model.vo.SelectedVo;
import edu.dlu.bysj.base.model.vo.UnSelectTopicVo;
import edu.dlu.bysj.base.model.vo.UnselectStudentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/31 11:37
 */
@Repository
public interface TopicMapper extends BaseMapper<Topics> {

    /**
     * 通过学生id 查询学生选题情况
     *
     * @param firstSubjectId  第一志愿id
     * @param secondSubjectId 第二志愿id
     * @return selectedVo已选题目结果列表
     */
    List<SelectedVo> selectStudentChooseInfo(@Param("firstSubjectId") String firstSubjectId,
                                             @Param("secondSubjectId") String secondSubjectId);


    /**
     * 查询该题目下的第一/第二志愿人数
     *
     * @param subjectId 题目id
     * @param type      该题目是第几志愿, 1 第一, 2第二
     * @return
     */
    List<UnselectStudentVo> firstAndSecondVolunteer(@Param("subjectId") Integer subjectId,
                                                    @Param("type") Integer type);


    /**
     * 通过学号/姓名模糊查询该专业该年级下没有确定题目的学生，
     *
     * @param studentNumber 学号
     * @param studentName   姓名
     * @param grade         年级
     * @param majorId       专业id
     * @return
     */
    List<UnselectStudentVo> unChooseTopicStudent(@Param("studentNumber") String studentNumber,
                                                 @Param("studentName") String studentName,
                                                 @Param("grade") Integer grade,
                                                 @Param("majorId") Integer majorId);

    /**
     * 查询未被选择的题目列表
     *
     * @param teacherNumber 教工号
     * @param teacherName   教师名称
     * @param grade         年级
     * @param majorId       专业id
     * @return
     */
    List<UnSelectTopicVo> unChooseSubject(@Param("teacherNumber") String teacherNumber,
                                          @Param("teacherName") String teacherName,
                                          @Param("grade") Integer grade,
                                          @Param("majorId") Integer majorId);
}
