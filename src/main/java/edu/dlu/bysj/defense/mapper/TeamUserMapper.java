package edu.dlu.bysj.defense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.TeamUser;
import edu.dlu.bysj.base.model.vo.ReplyInformationVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/7 10:32
 */
@Repository
public interface TeamUserMapper extends BaseMapper<TeamUser> {

    /**
     * 根据学生id,查询学生的所在分组信息
     *
     * @param studentId 学生id
     * @param isStudent 学生标识
     * @return ReplyInformationVo
     */
    ReplyInformationVo selectTeamInfoByStudentId(@Param("studentId") Integer studentId,
                                                 @Param("isStudentId") Integer isStudent);

    /**
     * 查看学生的年级
     *
     * @param studentId 学生id
     * @return 学生年级
     */
    Integer selectStudentGrade(Integer studentId);

    @MapKey("id")
    List<Map<String, Object>> groupMemberByGroupId(Integer groupId, Integer start, Integer size);

    Integer totalGroupMemberByGroupId(Integer groupId);

    @MapKey("")
    List<Map<String, String>> groupMemberInfo(List<Integer> ids);
}
