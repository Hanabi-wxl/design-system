package edu.dlu.bysj.grade.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.GoodTeacher;
import edu.dlu.bysj.base.model.query.MajorSearchQuery;
import edu.dlu.bysj.base.model.vo.MajorExcellentTeacherVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
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
    public TotalPackageVo<MajorExcellentTeacherVo> obtainGoodTeacherSelectionList(MajorSearchQuery query) {
        String teacherName = "", teacherNumber = "";
        if (query.getSearchContent().length() == 8)
            teacherNumber = query.getSearchContent();
        else
            teacherName = query.getSearchContent();
        int start = (query.getPageNumber()-1) * query.getPageSize();
        TotalPackageVo<MajorExcellentTeacherVo> packageVo = new TotalPackageVo<>();
        List<MajorExcellentTeacherVo> result = baseMapper.selectGoodTeacherInfo(query.getMajorId(), teacherNumber, teacherName,start,query.getPageSize());
        Integer total = baseMapper.selectTotalGoodTeacherInfo(query.getMajorId(), teacherNumber, teacherName);
        if (result != null && !result.isEmpty()) {

            for (MajorExcellentTeacherVo element : result) {

                GoodTeacher goodTeacher = baseMapper.selectOne(new QueryWrapper<GoodTeacher>()
                        .eq("college_agree",1)
                        .eq("teacher_id", element.getTeacherId())
                        .eq("school_year", query.getYear()));
                if (ObjectUtil.isNotNull(goodTeacher)) {
                    element.setIsGood(1);
                } else {
                    element.setIsGood(0);
                }
            }
        }
        packageVo.setArrays(result);
        packageVo.setTotal(total);
        return packageVo;
    }
}
