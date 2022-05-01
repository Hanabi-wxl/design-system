package edu.dlu.bysj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.vo.MajorSimpleInfoVo;
import edu.dlu.bysj.base.model.vo.MajorVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.model.vo.UserVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/21 20:36
 */
public interface MajorService extends IService<Major> {

    TotalPackageVo<MajorVo> majorPagination(String collegeId, Integer pageNumber, Integer pagesize);

    /**
     * 根据majorId 按顺序插入归档序号
     *
     * @param majorId 专业id
     * @return 插入是否成功
     */
    boolean generateFillingNumber(Integer majorId, Integer year);

    /**
     *
     *
     * @param collegeId 专业id
     * @return majorSimpleInfoVo结果集
     */
    List<MajorSimpleInfoVo> obtainCollegeMajor(Integer collegeId);


    /*
     * @Description: 获取该学院下的所有老师
     * @Author: sinre
     * @Date: 2022/4/29 22:44
     * @param collegeId 学院id
     * @return java.util.List<edu.dlu.bysj.base.model.vo.UserVo>
     **/
    List<UserVo> obtainCollegeTeacher(Integer collegeId);
}
