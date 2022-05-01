package edu.dlu.bysj.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Process;
import edu.dlu.bysj.base.model.vo.ProcessDetailVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/5 22:33
 */
@Repository
public interface ProcessMapper extends BaseMapper<Process> {

    /**
     * 通过subjectId查询process的详细信息
     *
     * @param subjectId 题目id
     * @return 过程管理详情类
     */
    List<ProcessDetailVo> selectProcessInfoBySubjectId(Integer subjectId);
}
