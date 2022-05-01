package edu.dlu.bysj.paper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Plan;
import edu.dlu.bysj.base.model.vo.ContentVo;
import edu.dlu.bysj.paper.mapper.PlanMapper;
import edu.dlu.bysj.paper.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/2 17:26
 */
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {
    private final PlanMapper planMapper;

    @Autowired
    public PlanServiceImpl(PlanMapper planMapper) {
        this.planMapper = planMapper;
    }

    @Override
    public List<ContentVo> checkPlans(Integer subjectId) {
        return planMapper.selectContentBySubejctId(subjectId);
    }
}
