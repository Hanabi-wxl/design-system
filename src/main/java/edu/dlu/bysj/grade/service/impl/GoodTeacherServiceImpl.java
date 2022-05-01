package edu.dlu.bysj.grade.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.GoodTeacher;
import edu.dlu.bysj.base.model.vo.MajorExcellentTeacherVo;
import edu.dlu.bysj.grade.mapper.GoodTeacherMapper;
import edu.dlu.bysj.grade.service.GoodTeacherService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/14 10:28
 */
@Service
public class GoodTeacherServiceImpl extends ServiceImpl<GoodTeacherMapper, GoodTeacher> implements GoodTeacherService {


    @Override
    public List<MajorExcellentTeacherVo> obtainGoodTeacherSelectionList(Integer majorId, Integer year, String teacherName, String teacherNumber) {
        List<MajorExcellentTeacherVo> result = baseMapper.selectGoodTeacherInfo(majorId, teacherNumber, teacherName);
        if (result != null && !result.isEmpty()) {

            for (MajorExcellentTeacherVo element : result) {

                GoodTeacher goodTeacher = baseMapper.selectOne(new QueryWrapper<GoodTeacher>()
                        .eq("teacher_id", element.getTeacherId())
                        .eq("school_year", year));
                if (ObjectUtil.isNotNull(goodTeacher)) {
                    element.setIsGood(1);
                } else {
                    element.setIsGood(0);
                }
            }
        }
        return result;
    }
}
