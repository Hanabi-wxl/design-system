package edu.dlu.bysj.defense.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.entity.Team;
import edu.dlu.bysj.base.model.enums.ResponsibilityEnum;
import edu.dlu.bysj.base.model.vo.ReplyTeacherVo;
import edu.dlu.bysj.base.model.vo.StudentGroupVo;
import edu.dlu.bysj.base.model.vo.TeacherSimplyVo;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import edu.dlu.bysj.defense.mapper.TeamMapper;
import edu.dlu.bysj.defense.service.TeamService;
import edu.dlu.bysj.system.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/7 9:44
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    private final MajorService majorService;

    private final TeacherMapper teacherMapper;

    @Autowired

    public TeamServiceImpl(MajorService majorService,
                           TeacherMapper teacherMapper) {
        this.majorService = majorService;
        this.teacherMapper = teacherMapper;
    }


    @Override
    public List<TeacherSimplyVo> otherMajorTeacherInfo(Integer majorId) {
        /*获取学院Id*/
        Major major = majorService.getById(majorId);
        Integer collegeId = major.getCollegeId();
        /*反馈该学院其他专业id*/
        List<Major> majorList = majorService.list(new QueryWrapper<Major>()
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
    public List<StudentGroupVo> studentReplyInfoOfSimilarGroup(Integer majorId, Integer grade, Integer isSecond) {
        List<StudentGroupVo> result = null;
        Team teamInfo = this.getOne(new QueryWrapper<Team>()
                .eq("major_id", majorId)
                .eq("grade", grade)
                .eq("is_repeat", isSecond));
        if (ObjectUtil.isNotNull(teamInfo)) {
            /*team_user 中 isStudent 1代表为学生,teamUser中的基础信息*/
            result = baseMapper.selectStudentInfofSimilarGroup(teamInfo.getId(), 1);

            if (result != null && !result.isEmpty()) {
                for (StudentGroupVo element : result) {
                    element.setGroupNumber(teamInfo.getTeamNumber());
                    element.setStartTime(teamInfo.getStartDate());
                    element.setEndTime(teamInfo.getEndTime());
                    element.setAddress(teamInfo.getAddress());
                    element.setRequirement(teamInfo.getRequest());
                    element.setRoleName(ResponsibilityEnum.getResponsibilityMessage(element.getRoleName()));
                }
            }
        }

        return result;
    }
}
