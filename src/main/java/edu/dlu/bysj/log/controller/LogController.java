package edu.dlu.bysj.log.controller;

import edu.dlu.bysj.base.model.entity.Log;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.log.service.LogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/22 22:51
 */
@RestController
@RequestMapping(value = "/systemManagement")
@Api(tags = "日志管理控制器")
public class LogController {

  @Autowired private LogService logService;

  @GetMapping(value = "/system/log/view/{type}/{year}")
  @LogAnnotation(content = "查看日志列表")
  public CommonResult<List<Log>> logView(
      @PathVariable("type") Integer type, @PathVariable("year") String year) {
    List<Log> list = null;
    if (!StringUtils.isEmpty(type)) {
      list = logService.allLogByYearAndType(type, year);
    }
    return CommonResult.success(list);
  }
}
