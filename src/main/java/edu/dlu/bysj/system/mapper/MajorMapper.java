package edu.dlu.bysj.system.mapper;

import java.util.List;
import java.util.Map;

import edu.dlu.bysj.base.model.vo.UserVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.vo.MajorSimpleInfoVo;
import edu.dlu.bysj.base.model.vo.MajorVo;

/**
 * @author XiangXinGang
 * @date 2021/10/21 20:36
 */
@Repository
public interface MajorMapper extends BaseMapper<Major> {
    /**
     * 根据collegeId 分页查询major list;
     *
     * @param collegeId
     *            专业id
     * @return majorVo
     */
    List<MajorVo> selectMajorList(String collegeId);

    /**
     * 根据majorId 按归档顺序获取学生以确定的题目id
     *
     * @param majorId
     * @return 学生以确定的题目id
     */
    List<Integer> archiveNumber(@Param("majorId") Integer majorId, @Param("grade") Integer grade);

    /**
     * 通过题目id跟新题目中的id状况;
     *
     * @param subjectId
     *            题目id
     * @param fillNumber
     *            :归档序号
     * @return 更新是否成功
     */
    Integer updateFillNumber(@Param("subjectId") Integer subjectId, @Param("fillNumber") Integer fillNumber);

    /**
     * 通过college 查询该学院下的专业;
     *
     * @param collegeId
     *            学院id
     * @return MajorSimpleInfo结果集;
     */
    List<MajorSimpleInfoVo> selectMajorInfoByCollegeId(Integer collegeId);

    /**
     * 查询该专业所属学院的所有专业;
     * 
     * @param majorId
     *            专业id
     * @return
     */
    @MapKey("id")
    Map<Integer, Map<String, Object>> selectAllMajorOfCollege(Integer majorId);

    List<UserVo> obtainCollegeTeacher(Integer collegeId);

    List<String> selectMajorNameByIds(List<Integer> majorIds);

}
