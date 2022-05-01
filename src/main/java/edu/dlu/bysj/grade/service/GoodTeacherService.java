package edu.dlu.bysj.grade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.GoodTeacher;
import edu.dlu.bysj.base.model.vo.MajorExcellentTeacherVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/14 10:27
 */
public interface GoodTeacherService extends IService<GoodTeacher> {

    /**
     * 通过教师id获取这个教师历年的goodTeacher 评优情况
     *
     * @param teacherId     教师id
     * @param year          年份
     * @param teacherName   教师名称
     * @param teacherNumber 教师id
     * @return MajorExcellentTeacherVo
     */
    List<MajorExcellentTeacherVo> obtainGoodTeacherSelectionList(Integer teacherId, Integer year, String teacherName, String teacherNumber);
}
