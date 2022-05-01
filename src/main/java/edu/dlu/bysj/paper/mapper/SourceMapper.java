package edu.dlu.bysj.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Source;
import edu.dlu.bysj.base.model.vo.SubjectSourceVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/28 20:09
 */
@Repository
public interface SourceMapper extends BaseMapper<Source> {
    /**
     * 搜索所有的题目来源
     *
     * @return 题目来源SubjectSourceVo 集合
     */
    List<SubjectSourceVo> selectAllSource();
}
