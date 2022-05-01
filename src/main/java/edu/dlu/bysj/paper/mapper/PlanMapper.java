package edu.dlu.bysj.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Plan;
import edu.dlu.bysj.base.model.vo.ContentVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/2 17:26
 */
@Repository
public interface PlanMapper extends BaseMapper<Plan> {
    /**
     * 查看该题目的周计划
     *
     * @param subjectId
     * @return
     */
    List<ContentVo> selectContentBySubejctId(Integer subjectId);
}
