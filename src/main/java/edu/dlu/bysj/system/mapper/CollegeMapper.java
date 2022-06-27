package edu.dlu.bysj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.College;
import edu.dlu.bysj.base.model.vo.CollegeSimpleInoVo;
import edu.dlu.bysj.document.entity.CollegeAnalysisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/18 16:35
 */
@Repository
public interface CollegeMapper extends BaseMapper<College> {

    /**
     * getCollegeByCondition
     *
     * @param college 查询条件
     * @return 查询结果大学对象;
     */
    College getCollegeByCondition(College college);

    /**
     * 获取该学校的所有学院id名称
     *
     * @param schoolId 学校id
     * @return 学院简单结果集和;
     */
    List<CollegeSimpleInoVo> getCollegeBySchoolId(Integer schoolId);

    /*
     * @Description:  根据专业id获取学院id
     * @Author: sinre
     * @Date: 2022/5/2 11:07
     * @param majorId
     * @return java.lang.Integer
     **/
    Integer getCollegeId(Integer majorId);

    Integer selectStudentCount(Integer collegeId);

}
