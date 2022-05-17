package edu.dlu.bysj.defense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.dto.EachMarkConvey;
import edu.dlu.bysj.base.model.entity.EachMark;
import edu.dlu.bysj.base.model.query.MutualEvaluationQuery;
import edu.dlu.bysj.base.model.vo.MutualEvaluationVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/6 9:37
 */
@Repository
public interface EachMarkMapper extends BaseMapper<EachMark> {

    /**
     * 通过MutualEvaluationQuery中包含的信息查询题目记录
     *
     * @param query    查询条件对象
     * @param start    分页开始位置
     * @param pageSize 每页记录数
     * @return List<MutualEvaluationVo>
     */
    List<MutualEvaluationVo> selectEachMarkInfoByQuery(@Param("query") MutualEvaluationQuery query,
                                                       @Param("start") Integer start,
                                                       @Param("pageSize") Integer pageSize);

    /**
     * 查询 selectEachMarkInfoByQuery对应的总数
     *
     * @param query 查询对象
     * @return 总数
     */
    Integer totalEachMarkInfoByQuery(@Param("query") MutualEvaluationQuery query);


    /**
     * 根据题目Id查询该题目被那位教师评价
     *
     * @param subjectId 题目ids
     * @return 教师名称
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> selectEachMarkTeacherName(@Param("subjectList") List<Integer> subjectId);


    /**
     * 该专业该年级所有教师指导题目
     *
     * @param grade   年级
     * @param majorId 专业id
     * @return 题目id，教师 id 题目的状态集合
     */
    List<EachMarkConvey> selectTeacherGuide(@Param("grade") Integer grade,
                                            @Param("majorId") Integer majorId);


    /**
     * 查询该subjectId 对应的teacher 的信息，并使用Map<Map<>> 方式返回第一层的map key 为subjectId
     *
     * @param subjectId 题目id
     * @return 该题目教师的信息集合
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> selectEachMarkTeacherBySubject(Integer subjectId);

    void removeBySubjectIds(List<Integer> subjectIds);

}
