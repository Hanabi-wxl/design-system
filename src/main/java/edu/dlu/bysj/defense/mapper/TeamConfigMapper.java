package edu.dlu.bysj.defense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.TeamConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/7 14:58
 */
@Repository
public interface TeamConfigMapper extends BaseMapper<TeamConfig> {
    /**
     * 查询答辩人与指导教师\互评教师同组的所有组号
     *
     * @param teacherId 指导教师id
     * @param grade     年级
     * @param majorId   专业id
     * @return 队伍的Id集合;
     */
    List<Integer> similarGuideOrOtherTeacher(@Param("teacherId") Integer teacherId,
                                             @Param("grade") Integer grade,
                                             @Param("majorId") Integer majorId);

    /**
     * 查询答辩人与指导教师不同组的情况;
     *
     * @param teacherId 教师Id
     * @param grade     年级
     * @param majorId   专业id
     * @return 组id
     */
    List<Integer> differentGuideTeacher(@Param("teacherId") Integer teacherId,
                                        @Param("grade") Integer grade,
                                        @Param("majorId") Integer majorId);
}
