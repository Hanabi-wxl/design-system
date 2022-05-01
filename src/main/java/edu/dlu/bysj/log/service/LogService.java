package edu.dlu.bysj.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Log;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/22 22:43
 */
public interface LogService extends IService<Log> {
  /**
   * 通过year, type 查询所有符合条件的log
   *
   * @param type 日志类型
   * @param year 年份
   * @return log集合：
   */
  List<Log> allLogByYearAndType(Integer type, String year);
}
