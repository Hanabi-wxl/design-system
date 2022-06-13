package edu.dlu.bysj.defense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Team;
import edu.dlu.bysj.base.model.vo.ReplyTeacherVo;
import edu.dlu.bysj.base.model.vo.StudentGroupVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/7 9:42
 */
@Repository
public interface TeamMapper extends BaseMapper<Team> {

    /**
     * 通过教师id,查询的年级确定返回信息
     *
     * @param teacherId 教师id
     * @param grade     年级
     * @param isStudent 是否为学生
     * @return 教师信息返回类
     */
    List<ReplyTeacherVo> selectTeacherGroupInfo(@Param("teacherId") Integer teacherId,
                                                @Param("grade") Integer grade,
                                                @Param("isStudent") Integer isStudent);

    /**
     * 根据教师的id grade查询该组的学生信息
     *
     * @param studentId
     * @return 学生分组信息集合;
     */
    StudentGroupVo selectStudentInfofSimilarGroup(Integer studentId);
}
