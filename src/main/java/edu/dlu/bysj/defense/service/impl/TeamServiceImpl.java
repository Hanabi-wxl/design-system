package edu.dlu.bysj.defense.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.entity.Team;
import edu.dlu.bysj.base.model.entity.TeamUser;
import edu.dlu.bysj.base.model.enums.ResponsibilityEnum;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.common.mapper.SubjectMapper;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import edu.dlu.bysj.defense.mapper.TeamMapper;
import edu.dlu.bysj.defense.mapper.TeamUserMapper;
import edu.dlu.bysj.defense.service.TeamService;
import edu.dlu.bysj.system.mapper.MajorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author XiangXinGang
 * @date 2021/11/7 9:44
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    private final MajorMapper majorMapper;

    private final TeacherMapper teacherMapper;

    private final SubjectMapper subjectMapper;

    private final TeamUserMapper teamUserMapper;

    @Autowired

    public TeamServiceImpl(MajorMapper majorMapper,TeamUserMapper teamUserMapper,
                           TeacherMapper teacherMapper,SubjectMapper subjectMapper) {
        this.teamUserMapper = teamUserMapper;
        this.majorMapper = majorMapper;
        this.subjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
    }


    @Override
    public List<TeacherSimplyVo> otherMajorTeacherInfo(Integer majorId) {
        /*获取学院Id*/
        Major major = majorMapper.selectById(majorId);
        Integer collegeId = major.getCollegeId();
        /*反馈该学院其他专业id*/
        List<Major> majorList = majorMapper.selectList(new QueryWrapper<Major>()
                .eq("college_id", collegeId)
                .ne("id", majorId));
        List<Integer> majorIds = new ArrayList<>();

        /*获取这些专业下的老师*/
        List<TeacherSimplyVo> result = null;
        if (majorList != null && majorList.size() > 0) {
            for (Major element : majorList) {
                majorIds.add(element.getId());
            }
            result = teacherMapper.pluralMajorTeacherInfoByMajorId(majorIds);
        }
        return result;
    }

    @Override
    public List<ReplyTeacherVo> teacherReplyInfo(Integer teacherId, Integer grade, Integer isStudent) {
        /*不同组中的信息*/
        List<ReplyTeacherVo> replyTeacherVos = baseMapper.selectTeacherGroupInfo(teacherId, grade, isStudent);
        /*翻译角色信息*/
        if (replyTeacherVos != null && !replyTeacherVos.isEmpty()) {
            for (ReplyTeacherVo element : replyTeacherVos) {
                element.setRoleName(ResponsibilityEnum.getResponsibilityMessage(element.getRoleName()));
            }
        }
        return replyTeacherVos;
    }

    @Override
    public List<StudentGroupVo> studentReplyInfoOfSimilarGroup(Integer majorId, Integer grade, Integer isSecond, Integer teacherId) {
        List<StudentGroupVo> result = new LinkedList<>();
        List<Map<String, Object>> maps = subjectMapper.selectMaps(new QueryWrapper<Subject>()
                .select("student_id")
                .eq("first_teacher_id", teacherId));
        for (Map<String, Object> map : maps) {
            if (ObjectUtil.isNotNull(map)){
                Object studentId = map.get("student_id");
                TeamUser teamUser = teamUserMapper.selectOne(new QueryWrapper<TeamUser>()
                        .eq("user_id", studentId)
                        .eq("is_student", 1));
                Team teamInfo = baseMapper.selectById(teamUser.getTeamId());
                StudentGroupVo groupVo = baseMapper.selectStudentInfofSimilarGroup(teamUser.getUserId());
                groupVo.setGroupNumber(teamInfo.getTeamNumber());
                groupVo.setStartTime(teamInfo.getStartDate());
                groupVo.setEndTime(teamInfo.getEndTime());
                groupVo.setAddress(teamInfo.getAddress());
                groupVo.setRequirement(teamInfo.getRequest());
                groupVo.setRoleName(ResponsibilityEnum.getResponsibilityMessage(groupVo.getRoleName()));
                result.add(groupVo);
            }
        }
        return result;
    }

    @Override
    public List<Integer> similarGuideTeacher(Integer teacherId, Integer grade) {
        List<TeamUser> teamUsers = teamUserMapper.selectList(new QueryWrapper<TeamUser>().eq("user_id", teacherId));
        List<Integer> ids = teamUsers.stream().map(TeamUser::getTeamId).collect(Collectors.toList());
        List<Team> teams = baseMapper.selectBatchIds(ids);
        ids.clear();
        teams.stream()
                .filter(item -> item.getGrade().equals(grade))
                .filter(item -> item.getType().equals(0))
                .forEach(item -> ids.add(item.getId()));
        return ids;
    }

    @Override
    public List<Integer> differentGuideTeacher(Integer firstTeacherId, Integer grade) {
        List<Team> teams = baseMapper.selectList(new QueryWrapper<Team>()
                .eq("grade", grade)
                .eq("type", 2));
        List<Integer> ids = teams.stream().map(Team::getId).collect(Collectors.toList());
        List<TeamUser> teamUsers = teamUserMapper.selectList(new QueryWrapper<TeamUser>().in("team_id", ids));
        List<Integer> collect = teamUsers.stream()
                .filter(item -> item.getUserId().equals(firstTeacherId))
                .map(TeamUser::getTeamId)
                .collect(Collectors.toList());
        ids = ids.stream().filter(item -> !collect.contains(item)).collect(Collectors.toList());
        return ids;
    }

    @Override
    public List<Integer> similarOtherTeacher(Integer otherPersonId, Integer grade) {
        List<TeamUser> teamUsers = teamUserMapper.selectList(new QueryWrapper<TeamUser>().eq("user_id", otherPersonId));
        List<Integer> ids = new LinkedList<>();
        teamUsers.forEach(item -> ids.add(item.getTeamId()));
        List<Team> teams = baseMapper.selectBatchIds(ids);
        ids.clear();
        teams.stream()
                .filter(item -> item.getGrade().equals(grade))
                .filter(item -> item.getType().equals(1))
                .forEach(item -> ids.add(item.getId()));
        return ids;
    }
}
