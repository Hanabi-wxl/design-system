package edu.dlu.bysj.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Title;

import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/10/24 22:24
 */
public interface TitleService extends IService<Title> {
  /**
   * 查询所有的职称,id ,name
   *
   * @return 查询map集合
   */
  List<Map<String, Object>> allTitleSimplify();
}
