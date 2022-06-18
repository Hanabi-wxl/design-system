package edu.dlu.bysj.grade.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import edu.dlu.bysj.base.model.dto.SubjectInfoConvey;
import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.vo.GroupScoreVo;
import edu.dlu.bysj.base.model.vo.ScoreSummaryVo;
import edu.dlu.bysj.base.model.vo.TeacherYearEvaluationVo;

/**
 * @author XiangXinGang
 * @date 2021/11/12 22:33
 */
@Repository
public interface ScoreMapper extends BaseMapper<Score> {
    /**
     * 通过答辩分组id查询该分组内学生的成绩信息
     *
     * @param teamId
     *            题目id
     * @return
     */
    List<GroupScoreVo> selectScoreByTeam(Integer teamId);

    /**
     * 按区间搜索分数;
     *
     * @param teacherId
     *            教师id
     * @return
     */
    List<TeacherYearEvaluationVo> selectStudentNumByScore(Integer teacherId);

    /**
     * 查询学生选题信息
     *
     * @param teacherId
     *            教师id
     * @param grade
     *            年份;
     * @return
     */
    List<SubjectInfoConvey> selectSubjectByTeacherIdAndYear(Integer teacherId, Integer grade);

    /**
     * 搜索该专业下的所有成绩详情
     *
     * @param majorId
     *            专业id
     * @param yar
     *            年份
     * @return
     */
    ScoreSummaryVo selectByMajorId(@Param("majorId") Integer majorId, @Param("year") Integer yar);
}
