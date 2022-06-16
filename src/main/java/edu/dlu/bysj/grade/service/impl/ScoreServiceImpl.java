package edu.dlu.bysj.grade.service.impl;

import java.util.*;

import edu.dlu.bysj.base.model.query.GroupMemberQuery;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.defense.mapper.TeamUserMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.dto.SubjectInfoConvey;
import edu.dlu.bysj.base.model.entity.GoodTeacher;
import edu.dlu.bysj.base.model.entity.Score;
import edu.dlu.bysj.base.model.enums.FractionalSegmentEnum;
import edu.dlu.bysj.base.model.vo.basic.BasicExcellentVo;
import edu.dlu.bysj.grade.mapper.GoodTeacherMapper;
import edu.dlu.bysj.grade.mapper.ScoreMapper;
import edu.dlu.bysj.grade.service.ScoreService;

/**
 * @author XiangXinGang
 * @date 2021/11/12 22:34
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    private final GoodTeacherMapper goodTeacherMapper;

    private final TeamUserMapper teamUserMapper;

    public ScoreServiceImpl(GoodTeacherMapper goodTeacherMapper,TeamUserMapper teamUserMapper) {
        this.teamUserMapper = teamUserMapper;
        this.goodTeacherMapper = goodTeacherMapper;
    }

    @Override
    public List<GroupScoreVo> scoreOfSubjectTeam(Integer teamId) {
        return baseMapper.selectScoreByTeam(teamId);
    }

    @Override
    public Integer sumScore(Score score) {
        int sum = 0;
        sum = (score.getProcessAttitude() + score.getProcessDiscipline() + score.getProcessReport()
            + score.getProcessComplete() + score.getFirstQuality() + score.getFirstAbility()
            + score.getFirstComplete() + score.getOtherQuality() + score.getOtherAbility() + score.getOtherComplete()
            + score.getTotalSelfSummary() + score.getTotalProcess() + score.getTotalQuality()
            + score.getTotalCompleteQuality() + score.getTotalAbility());
        return sum;
    }

    @Override
    public List<TeacherYearEvaluationVo> studentScoreNum(Integer teacherId) {
        return baseMapper.selectStudentNumByScore(teacherId);
    }

    @Override
    public ExcellentTeacherTableVo excellentInfoOfYear(Integer teacherId, Integer year) {
        List<SubjectInfoConvey> subjectInfoConveys = baseMapper.selectSubjectByTeacherIdAndYear(teacherId, year);
        ExcellentTeacherTableVo result = new ExcellentTeacherTableVo();
        result.setTotal(0);
        if (subjectInfoConveys != null && !subjectInfoConveys.isEmpty()) {
            result.setTotal(subjectInfoConveys.size());
            /*是否评优*/
            GoodTeacher goodTeacher = goodTeacherMapper
                .selectOne(new QueryWrapper<GoodTeacher>().eq("teacher_id", teacherId).eq("school_year", year));

            /*默认没有评优*/
            result.setIsGood(0);

            /*修改评优状态*/
            if (ObjectUtil.isNotNull(goodTeacher)) {
                result.setIsGood(1);
                result.setComment(goodTeacher.getSelfComment());
            }
            List<BasicExcellentVo> array = new ArrayList<>();
            /*替换成绩分数*/
            for (SubjectInfoConvey element : subjectInfoConveys) {
                BasicExcellentVo basicExcellentVo = new BasicExcellentVo();
                basicExcellentVo.setStudentName(element.getStudentName());
                basicExcellentVo.setSubjectName(element.getSubjectName());
                String scoreMapping = FractionalSegmentEnum.getScoreMapping(element.getScore());
                basicExcellentVo.setScore(scoreMapping);
            }

            result.setInfo(array);
        }

        return result;
    }

    @Override
    public ScoreSummaryVo summaryScorePercentage(Integer majorId, Integer year) {
        ScoreSummaryVo result = baseMapper.selectByMajorId(majorId, year);
        /*换算成百分比*/
        if (ObjectUtil.isNotNull(result)) {
            /*优秀*/
            result.setFirstOutstanding(result.getFirstOutstanding() * 100);
            result.setProcessOutstanding(result.getProcessOutstanding() * 100);
            result.setOtherOutstanding(result.getOtherOutstanding() * 100);
            result.setTotalOutstanding(result.getTotalOutstanding() * 100);
            /*良好*/
            result.setFirstGood(result.getFirstGood() * 100);
            result.setProcessGood(result.getProcessGood() * 100);
            result.setOtherGood(result.getOtherGood() * 100);
            result.setTotalGood(result.getTotalGood() * 100);
            /*中等*/
            result.setFirstMiddle(result.getFirstMiddle() * 100);
            result.setProcessMiddle(result.getProcessMiddle() * 100);
            result.setOtherMiddle(result.getOtherMiddle() * 100);
            result.setTotalMiddle(result.getTotalMiddle() * 100);
            /*较差*/
            result.setFirstBad(result.getFirstBad() * 100);
            result.setProcessBad(result.getProcessBad() * 100);
            result.setOtherBad(result.getOtherBad() * 100);
            result.setTotalBad(result.getTotalBad() * 100);
            /*不及格*/
            result.setFirstFailed(result.getFirstFailed() * 100);
            result.setProcessFailed(result.getProcessFailed() * 100);
            result.setOtherFailed(result.getOtherFailed() * 100);
            result.setTotalFailed(result.getTotalFailed() * 100);
            /*没有分数默认为0分即为没有打分(不及格中包含未打分部分)*/
            result.setFirstNoScore(result.getFirstNoScore() * 100);
            result.setProcessNoScore(result.getProcessNoScore() * 100);
            result.setOtherNoScore(result.getOtherNoScore() * 100);
            result.setTotalNoScore(result.getTotalNoScore() * 100);
        }

        return result;
    }

    @Override
    public TotalPackageVo<Map<String, Object>> obtainGroupMember(GroupMemberQuery query) {
        TotalPackageVo<Map<String, Object>> packageVo = new TotalPackageVo<>();
        /*分页*/
        List<Map<String,Object>> groupMembers = teamUserMapper.groupMemberByGroupId(
                query.getGroupId(),
                (query.getPageNumber() - 1) * query.getPageSize(),
                query.getPageSize());
        /*总数*/
        Integer total = teamUserMapper.totalGroupMemberByGroupId(query.getGroupId());
        List<Integer> ids = new LinkedList<>();
        for (Map<String, Object> groupMember : groupMembers) {
            ids.add(Integer.parseInt(String.valueOf(groupMember.get("subjectId"))));
        }
        if (ids.size() != 0){
            List<Map<String,String>> groupMemberInfo = teamUserMapper.groupMemberInfo(ids);
            Iterator<Map<String, Object>> iterator = groupMembers.iterator();
            Iterator<Map<String, String>> iterator1 = groupMemberInfo.iterator();
            while (iterator.hasNext()){
                Map<String, Object> next = iterator.next();
                Map<String, String> next1 = iterator1.next();
                next.put("progress",next1.get("progress"));
                next.put("defenceScore",next1.get("defenceScore"));
            }
            packageVo.setArrays(groupMembers);
            packageVo.setTotal(total);
        }
        return packageVo;
    }
}
