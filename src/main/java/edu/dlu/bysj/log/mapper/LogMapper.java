package edu.dlu.bysj.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Log;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/22 22:44
 */
@Repository
public interface LogMapper extends BaseMapper<Log> {
  List<Log> selectAllByDateAndType(Integer type, String year);
}
