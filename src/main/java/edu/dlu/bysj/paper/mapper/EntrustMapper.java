package edu.dlu.bysj.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Entrust;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/31 8:54
 */
@Repository
public interface EntrustMapper extends BaseMapper<Entrust> {

    /**
     * 查询该年份下的被委托人列表
     *
     * @param consigneeId 被委托人列表
     * @param year        年份
     * @return 题目id集合
     */
    List<Integer> selectByConsigneeIdAndDate(Integer consigneeId, String year);
}
