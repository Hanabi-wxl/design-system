package edu.dlu.bysj.paper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Entrust;
import edu.dlu.bysj.base.model.vo.EntrustInfoVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.common.mapper.SubjectMapper;
import edu.dlu.bysj.paper.mapper.EntrustMapper;
import edu.dlu.bysj.paper.service.EntrustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/31 8:55
 */
@Service
public class EntrustServiceImpl extends ServiceImpl<EntrustMapper, Entrust> implements EntrustService {

    private final EntrustMapper entrustMapper;

    private final SubjectMapper subjectMapper;

    @Autowired
    public EntrustServiceImpl(EntrustMapper entrustMapper,
                              SubjectMapper subjectMapper) {
        this.entrustMapper = entrustMapper;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public TotalPackageVo<EntrustInfoVo> selfEntrusts(Integer consigneeId, Integer year, Integer pageNumber, Integer pageSize) {
        /*获取被委托题目id*/
        List<Integer> list = entrustMapper.selectByConsigneeIdAndDate(consigneeId, year);
        Integer start = (pageNumber - 1) * pageSize;
        List<EntrustInfoVo> entrustInfoVos = subjectMapper.entrustSubjectPagination(list, start, pageSize);

        Integer total = subjectMapper.totalEntrustSubject(list);
        TotalPackageVo<EntrustInfoVo> result = new TotalPackageVo<>();
        result.setTotal(total);
        result.setArrays(entrustInfoVos);

        return result;
    }
}
