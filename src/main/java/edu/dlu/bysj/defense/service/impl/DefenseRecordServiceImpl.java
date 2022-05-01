package edu.dlu.bysj.defense.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.DefenceRecord;
import edu.dlu.bysj.base.model.query.DefenseRecordQuery;
import edu.dlu.bysj.base.model.vo.SimilarTeamStudentVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.defense.mapper.DefenceRecordMapper;
import edu.dlu.bysj.defense.service.DefenseRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/8 15:18
 */
@Service
public class DefenseRecordServiceImpl extends ServiceImpl<DefenceRecordMapper, DefenceRecord> implements DefenseRecordService {

    @Override
    public TotalPackageVo<SimilarTeamStudentVo> studentInfoOfTeam(DefenseRecordQuery query) {
        TotalPackageVo<SimilarTeamStudentVo> result = new TotalPackageVo<>();

        Integer start = (query.getPageNumber() - 1) * query.getPageSize();
        List<SimilarTeamStudentVo> similarTeamStudentVos = baseMapper.studentInfoOfGroup(query.getGroupNumber(), query.getMajorId(), query.getGrade(), start, query.getPageSize());
        Integer total = baseMapper.totalInfoOfGroup(query.getGroupNumber(), query.getMajorId(), query.getGrade());
        result.setTotal(total);
        result.setArrays(similarTeamStudentVos);
        return result;
    }
}
