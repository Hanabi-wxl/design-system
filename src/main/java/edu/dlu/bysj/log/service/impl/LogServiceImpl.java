package edu.dlu.bysj.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Log;
import edu.dlu.bysj.log.mapper.LogMapper;
import edu.dlu.bysj.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/22 22:44
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
  @Autowired private LogMapper logMapper;

  @Override
  public List<Log> allLogByYearAndType(Integer type, String year) {
    return logMapper.selectAllByDateAndType(type, year);
  }
}
