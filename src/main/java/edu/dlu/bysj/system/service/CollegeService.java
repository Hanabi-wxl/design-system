package edu.dlu.bysj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.College;
import edu.dlu.bysj.base.model.query.basic.CommonPage;
import edu.dlu.bysj.base.model.vo.CollegeSimpleInoVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/18 16:39
 */
public interface CollegeService extends IService<College> {
    /**
     * 使用redis分页操作
     *
     * @param page     每页记录数，和当前页数
     * @param schoolId 学校id
     * @return 分页打包对象
     */
    TotalPackageVo<College> collegePagination(CommonPage page, Integer schoolId);

    /**
     * @param college 学院;
     * @return
     */
    College findCollegeObject(College college);


    /**
     * 通过schoolId 获取学院name,id
     *
     * @param schoolId 学校id
     * @return CollegeSimpleInfo 信息
     */
    List<CollegeSimpleInoVo> obtainCollegeInfoBySchool(Integer schoolId);

    Integer getCollegeIdByMajorId(Integer majorId);
}
