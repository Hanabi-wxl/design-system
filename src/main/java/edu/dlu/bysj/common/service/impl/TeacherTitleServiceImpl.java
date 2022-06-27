package edu.dlu.bysj.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.TeacherTitle;
import edu.dlu.bysj.common.mapper.TeacherTitleMapper;
import edu.dlu.bysj.common.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/10/24 22:25
 */
@Service
public class TeacherTitleServiceImpl extends ServiceImpl<TeacherTitleMapper, TeacherTitle> implements TitleService {
  @Autowired private TeacherTitleMapper teacherTitleMapper;

  @Override
  public List<Map<String, Object>> allTitleSimplify() {
    Map<Integer, Map<String, Object>> integerMapMap = teacherTitleMapper.selectAll();
    // 转换为listMap
    List<Map<String, Object>> result = new ArrayList<>();
    for (Integer key : integerMapMap.keySet()) {
      result.add(integerMapMap.get(key));
    }
    return result;
  }
}
