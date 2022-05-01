package edu.dlu.bysj.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Process;
import edu.dlu.bysj.base.model.vo.ProcessDetailVo;

import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/5 22:33
 */
public interface ProcessService extends IService<Process> {

    /**
     * 通过subjectId获取该体下的过程检查详情
     *
     * @param subjectId 题目id
     * @return 过程检查详情类集合
     */
    List<ProcessDetailVo> processDetail(Integer subjectId);


    /**
     * 获取改题目的过程检查点完成情况;
     *
     * @param subjectId 题目Id
     * @return 使用map集合封装 week,status
     */
    List<Map<String, Object>> processStatus(Integer subjectId);
}
