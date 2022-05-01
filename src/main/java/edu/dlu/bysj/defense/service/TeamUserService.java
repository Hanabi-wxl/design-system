package edu.dlu.bysj.defense.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.TeamUser;
import edu.dlu.bysj.base.model.vo.ReplyInformationVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/7 10:33
 */
public interface TeamUserService extends IService<TeamUser> {
    /**
     * 调整学生的组内序号、和分组情况;
     *
     * @param studentId 学生id
     * @param groupId   组号
     * @param serial    组内序号
     * @param isStudent 是否为学生
     * @return 调整是否成功
     */
    boolean adjustTeamUserOfStudent(Integer studentId, Integer groupId, Integer serial, Integer isStudent);

    /**
     * 获取该学生的答辩分组信息
     *
     * @param studentId 学生id
     * @param isStudent 是否是学生
     * @return ReplyInformationVo类
     */
    ReplyInformationVo checkDefenseGroupOfStudent(Integer studentId, Integer isStudent);


    /**
     * 检查学生在申请答辩前的所有未完成的环节;
     *
     * @param subjectId 题目Id
     * @param userId    学生id
     * @return 未完成信息集合
     */
    List<String> undoneLink(Integer subjectId, Integer userId);


    /**
     * 将申请答辩的学生插入到对应的答辩组中
     *
     * @param studentId    学生id
     * @param majorId      专业id
     * @param resposiblity 答辩角色
     * @param subjectId    题目id
     * @return 插入是否成功
     */
    boolean addReplyStudent(Integer studentId, Integer majorId, Integer resposiblity, Integer subjectId);


}
