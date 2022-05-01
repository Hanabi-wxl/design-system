package edu.dlu.bysj.defense.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.DefenceRecord;
import edu.dlu.bysj.base.model.query.DefenseRecordQuery;
import edu.dlu.bysj.base.model.vo.SimilarTeamStudentVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;

/**
 * @author XiangXinGang
 * @date 2021/11/8 15:17
 */
public interface DefenseRecordService extends IService<DefenceRecord> {

    /**
     * 根据查询对象分页查询该组内的学生题目相关信息;
     *
     * @param query 查询对象
     * @return 封装的分页查询对象；
     */
    TotalPackageVo<SimilarTeamStudentVo> studentInfoOfTeam(DefenseRecordQuery query);
}
