package edu.dlu.bysj.grade.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.vo.ExcellentTeacherTableVo;
import edu.dlu.bysj.base.model.vo.GroupScoreVo;
import edu.dlu.bysj.base.model.vo.ScoreSummaryVo;
import edu.dlu.bysj.base.model.vo.TeacherYearEvaluationVo;

/**
 * @author XiangXinGang
 * @date 2021/11/12 22:33
 */
public interface ScoreService extends IService<Score> {

    /**
     * 通过答辩分组id 查看该组的成绩
     *
     * @param teamId
     *            队伍id
     * @return 各项成绩类集合
     */
    List<GroupScoreVo> scoreOfSubjectTeam(Integer teamId);

    /**
     * 汇总成绩
     *
     * @param score
     *            成绩对象
     * @return 成绩总分;
     */
    Integer sumScore(Score score);

    /**
     * 通过teacherId查询该教师在所有年份的带过的学生
     *
     * @param teacherId
     *            教师id
     * @return
     */
    List<TeacherYearEvaluationVo> studentScoreNum(Integer teacherId);

    /**
     * 查看优秀教师申报表的信息
     *
     * @param teacherId
     *            教师id
     * @param year
     *            年份
     * @return
     */
    ExcellentTeacherTableVo excellentInfoOfYear(Integer teacherId, Integer year);

    /**
     * 获取该专业的百分比
     *
     * @param majorId
     *            专业id
     * @param year
     *            年份
     * @return
     */
    ScoreSummaryVo summaryScorePercentage(Integer majorId, Integer year);
}
