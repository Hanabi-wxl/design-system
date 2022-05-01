package edu.dlu.bysj.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Plan;
import edu.dlu.bysj.base.model.vo.ContentVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/2 17:26
 */
public interface PlanService extends IService<Plan> {

    /**
     * 查看题目的周计划通过subjectId
     *
     * @param subjectId 题目id
     * @return 周计划表
     */
    List<ContentVo> checkPlans(Integer subjectId);
}
