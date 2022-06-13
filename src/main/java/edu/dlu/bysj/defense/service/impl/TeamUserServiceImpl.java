package edu.dlu.bysj.defense.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.*;
import edu.dlu.bysj.base.model.vo.ReplyInformationVo;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.defense.mapper.TeamConfigMapper;
import edu.dlu.bysj.defense.mapper.TeamUserMapper;
import edu.dlu.bysj.defense.service.TeamConfigService;
import edu.dlu.bysj.defense.service.TeamService;
import edu.dlu.bysj.defense.service.TeamUserService;
import edu.dlu.bysj.grade.service.ScoreService;
import edu.dlu.bysj.paper.service.FileInformationService;
import edu.dlu.bysj.paper.service.MiddleCheckService;
import edu.dlu.bysj.paper.service.OpenReportService;
import edu.dlu.bysj.paper.service.TaskBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author XiangXinGang
 * @date 2021/11/7 10:33
 */
@Service
public class TeamUserServiceImpl extends ServiceImpl<TeamUserMapper, TeamUser> implements TeamUserService {
    private final TeamConfigService teamConfigService;

    private final ScoreService scoreService;

    private final FileInformationService fileInformationService;

    private final MiddleCheckService middleCheckService;

    private final OpenReportService openReportService;

    private final TaskBookService taskBookService;

    private final SubjectService subjectService;

    private final StudentService studentService;

    private final TeamConfigMapper teamConfigMapper;

    private final TeamService teamService;


    @Autowired
    public TeamUserServiceImpl(SubjectService subjectService,
                               StudentService studentService,
                               TaskBookService taskBookService,
                               OpenReportService openReportService,
                               MiddleCheckService middleCheckService,
                               FileInformationService fileInformationService,
                               ScoreService scoreService,
                               TeamConfigService teamConfigService,
                               TeamConfigMapper teamConfigMapper,
                               TeamService teamService) {
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.taskBookService = taskBookService;
        this.openReportService = openReportService;
        this.middleCheckService = middleCheckService;
        this.fileInformationService = fileInformationService;
        this.scoreService = scoreService;
        this.teamConfigService = teamConfigService;
        this.teamConfigMapper = teamConfigMapper;
        this.teamService = teamService;
    }

    @Override
    public boolean adjustTeamUserOfStudent(Integer studentId, Integer groupId, Integer serial, Integer isStudent) {
        /*原来的组*/
        TeamUser teamUser = this.getOne(new QueryWrapper<TeamUser>()
                .eq("user_id", studentId)
                .eq("is_student", isStudent));
        if (ObjectUtil.isNotNull(teamUser)) {
            /*插入新的值*/
            TeamUser targetTeamUser = new TeamUser();
            targetTeamUser.setId(teamUser.getId());
            targetTeamUser.setUserId(studentId);
            targetTeamUser.setTeamId(groupId);
            targetTeamUser.setSerial(serial);
            targetTeamUser.setResposiblity(4);
            targetTeamUser.setIsStudent(isStudent);
            baseMapper.updateById(targetTeamUser);
        }
        return true;
    }

    @Override
    public ReplyInformationVo checkDefenseGroupOfStudent(Integer studentId, Integer isStudent) {
        return baseMapper.selectTeamInfoByStudentId(studentId, isStudent);
    }

    @Override
    public List<String> undoneLink(Integer subjectId, Integer userId) {
        List<String> need = new ArrayList<>();
        // TODO 验证 1-18 环节是否完成，从开始申报题目到申请答辩
        Subject studentSubject = subjectService.getById(subjectId);

        Student user = studentService.getById(userId);

        if (ObjectUtil.isNull(studentSubject)) {
            need.add("题目申报未完成");
        } else {
            if (ObjectUtil.isNull(studentSubject.getMajorAgree())) {
                need.add("题目专业审核未完成");
            }
            if (ObjectUtil.isNull(studentSubject.getCollegeAgree())) {
                need.add("题目院级审核未完成");
            }
            if (!subjectId.equals(user.getSubjectId())) {
                need.add("选题未完成");
            }
        }

        TaskBook taskBook = taskBookService.getOne(new QueryWrapper<TaskBook>().eq("subject_id", subjectId));
        if (ObjectUtil.isNull(taskBook)) {
            need.add("教师任务书未填写");
        } else {
            if (ObjectUtil.isNull(taskBook.getMajorAgree())) {
                need.add("任务书专业级审核未完成");
            }
            if (ObjectUtil.isNull(taskBook.getCollegeAgree())) {
                need.add("任务书院级审核未完成");
            }
        }

        OpenReport openReport = openReportService.getOne(new QueryWrapper<OpenReport>().eq("subject_id", subjectId));

        if (ObjectUtil.isNull(openReport)) {
            need.add("未提交开题报告");
        } else {
            if (ObjectUtil.isNull(openReport.getTeacherComment())) {
                need.add("开题报告自查未完成");
            }

            if (ObjectUtil.isNull(openReport.getMajorAgree())) {
                need.add("开题报告专业级审核未完成");
            }
            if (ObjectUtil.isNull(openReport.getCollegeAgree())) {
                need.add("开题报告院级审核未完成");
            }
        }

        MiddleCheck middleCheck = middleCheckService.getOne(new QueryWrapper<MiddleCheck>().eq("subject_id", subjectId));
        if (ObjectUtil.isNull(middleCheck)) {
            need.add("中期检查未提交");
        } else {
            if (ObjectUtil.isNull(middleCheck.getMajorAgree())) {
                need.add("中期检查专业级审核未完成");
            }
            if (ObjectUtil.isNull(middleCheck.getCollegeAgree())) {
                need.add("中期检查院级审核未完成");
            }
        }
        FileInfomation file = fileInformationService.getOne(new QueryWrapper<FileInfomation>()
                .eq("user_id", userId)
                .eq("is_student", 1)
                .eq("type", "2"));

        if (ObjectUtil.isNull(file)) {
            need.add("论文未提交");
        }

        Score score = scoreService.getOne(new QueryWrapper<Score>()
                .eq("subject_id", subjectId));

        if (ObjectUtil.isNull(score)) {
            need.add("指导教师评价-过程评价-互评均未完成");
        } else {
            if (ObjectUtil.isNull(score.getFirstAbility()) || ObjectUtil.isNull(score.getFirstComment())
                    || ObjectUtil.isNull(score.getFirstComplete()) || ObjectUtil.isNull(score.getFirstDate())
                    || ObjectUtil.isNull(score.getFirstQuality())) {
                need.add("指导教师评价未完成");
            }

            if (ObjectUtil.isNull(score.getProcessAttitude()) || ObjectUtil.isNull(score.getProcessDiscipline())
                    || ObjectUtil.isNull(score.getProcessReport()) || ObjectUtil.isNull(score.getProcessComment())
                    || ObjectUtil.isNull(score.getProcessComplete()) || ObjectUtil.isNull(score.getProcessDate())) {
                need.add("过程评价未完成");
            }

            if (ObjectUtil.isNull(score.getOtherQuality()) || ObjectUtil.isNull(score.getOtherAbility())
                    || ObjectUtil.isNull(score.getOtherComment()) || ObjectUtil.isNull(score.getOtherComplete())
                    || ObjectUtil.isNull(score.getOtherPersonId()) || ObjectUtil.isNull(score.getOtherDate())) {
                need.add("互评未完成");
            }
        }
        return need;
    }


    @Override
    public boolean addReplyStudent(Integer studentId, Integer majorId, Integer resposiblity, Integer subjectId) {

        Integer grade = baseMapper.selectStudentGrade(studentId);
        /*查询答辩配置*/
        List<Team> teams = teamService.list(new QueryWrapper<Team>()
                .eq("grade", grade)
                .eq("major_id", majorId));
        Set<Integer> ids = new HashSet<>();
        for (Team team : teams) {
            ids.add(team.getType());
        }
        Iterator<Integer> iterator = ids.iterator();
        List<Integer> idList = new ArrayList<>();
        while (iterator.hasNext()){
            idList.add(iterator.next());
        }
        Random random = new Random();
        int type = idList.get(random.nextInt(idList.size()));
        /*根据分组规则插入分组*/
        if(ObjectUtil.isNull(teams))
            return false;
        return this.addRespondentByRule(studentId, type, resposiblity, subjectId, grade, majorId);
    }

    private boolean addRespondentByRule(Integer studentId, Integer configRule, Integer resposiblity, Integer subjectId, Integer grade, Integer majorId) {
        Subject subject = subjectService.getById(subjectId);
        TeamUser teamUser = new TeamUser();
        teamUser.setUserId(studentId);
        teamUser.setIsStudent(1);
        teamUser.setResposiblity(resposiblity);
        if (configRule.equals(0)) {
            /*与指导教师同组*/
            List<Integer> teamIds = teamConfigMapper.similarGuideOrOtherTeacher(subject.getFirstTeacherId(), grade, majorId);
            this.fillingSerialAndTeamId(teamUser, teamIds);

        } else if (configRule.equals(1)) {
            /*互评教师与学生同组*/
            Score score = scoreService.getOne(new QueryWrapper<Score>().eq("subject_id", subjectId));
            Integer otherPersonId = score.getOtherPersonId();
            List<Integer> teamIds = teamConfigMapper.similarGuideOrOtherTeacher(otherPersonId, grade, majorId);
            this.fillingSerialAndTeamId(teamUser, teamIds);

        } else if (configRule.equals(2)) {
            /*不与指导教师同组*/
            List<Integer> teamIds = teamConfigMapper.differentGuideTeacher(subject.getFirstTeacherId(), grade, majorId);
            this.fillingSerialAndTeamId(teamUser, teamIds);

        } else {
            /*随机分配*/
            List<Team> teams = teamService.list(new QueryWrapper<Team>()
                    .eq("grade", grade)
                    .eq("major_id", majorId));
            int i = RandomUtil.randomInt(0, teams.size());
            teamUser.setTeamId(teams.get(i).getId());
            int count = this.count(new QueryWrapper<TeamUser>()
                    .eq("team_id", teams.get(i).getId())
                    .eq("is_student", 1));
            teamUser.setSerial(count + 1);
        }

        if (ObjectUtil.isNull(teamUser.getTeamId()) || ObjectUtil.isNull(teamUser.getSerial())) {
            return false;
        }
        return this.save(teamUser);
    }

    private void fillingSerialAndTeamId(TeamUser teamUser, List<Integer> teamIds) {
        int i = RandomUtil.randomInt(0, teamIds.size());
        teamUser.setTeamId(teamIds.get(i));
        /*统计组内序号*/
        int count = this.count(new QueryWrapper<TeamUser>()
                .eq("team_id", teamIds.get(i))
                .eq("is_student", 1));
        teamUser.setSerial(count + 1);
    }

}
