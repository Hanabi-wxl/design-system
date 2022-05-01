package edu.dlu.bysj.grade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.GoodTeacher;
import edu.dlu.bysj.base.model.vo.MajorExcellentTeacherVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/14 10:27
 */
@Repository
public interface GoodTeacherMapper extends BaseMapper<GoodTeacher> {
    /**
     * 教师id查询该教师的历年情况
     *
     * @param majorId       教师id
     * @param TeacherNumber 教师编号
     * @param teacherName   教师名称
     * @return
     */
    List<MajorExcellentTeacherVo> selectGoodTeacherInfo(@Param("majorId") Integer majorId,
                                                        @Param("teacherNumber") String TeacherNumber,
                                                        @Param("teacherName") String teacherName);
}
