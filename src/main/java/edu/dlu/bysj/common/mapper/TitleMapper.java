package edu.dlu.bysj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Title;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/10/24 22:24
 */
@Repository
public interface TitleMapper extends BaseMapper<Title> {

  /**
   * 查询全部职称的id和名称;
   *
   * @return
   */
  @MapKey("id")
  Map<Integer, Map<String, Object>> selectAll();
}
