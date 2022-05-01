package edu.dlu.bysj.defense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.DefenceRecord;
import edu.dlu.bysj.base.model.vo.SimilarTeamStudentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/8 15:17
 */
@Repository
public interface DefenceRecordMapper extends BaseMapper<DefenceRecord> {

    /**
     * 查询该分组下的学生答辩信息
     *
     * @param teamNumber 分组号
     * @param majorId    专业id
     * @param grade      年级
     * @param start      分页开始位置
     * @param pageSize   每页记录数
     * @return SimilarTeamStudentVo集合
     */
    List<SimilarTeamStudentVo> studentInfoOfGroup(@Param("teamNumber") Integer teamNumber,
                                                  @Param("majorId") Integer majorId,
                                                  @Param("grade") Integer grade,
                                                  @Param("start") Integer start,
                                                  @Param("pageSize") Integer pageSize);

    /**
     * 查询 studentInfoOfGroup的总数
     *
     * @param teamNumber 组号
     * @param majorId    专业id
     * @param grade      年级
     * @return
     */
    Integer totalInfoOfGroup(@Param("teamNumber") Integer teamNumber,
                             @Param("majorId") Integer majorId,
                             @Param("grade") Integer grade);
}
