package edu.dlu.bysj.grade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.GoodTeacher;
import edu.dlu.bysj.base.model.query.MajorSearchQuery;
import edu.dlu.bysj.base.model.vo.MajorExcellentTeacherVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;

/**
 * @author XiangXinGang
 * @date 2021/11/14 10:27
 */
public interface GoodTeacherService extends IService<GoodTeacher> {

    /**
     * 通过教师id获取这个教师历年的goodTeacher 评优情况
     * @return MajorExcellentTeacherVo
     */
    TotalPackageVo<MajorExcellentTeacherVo> obtainGoodTeacherSelectionList(MajorSearchQuery query);
}
