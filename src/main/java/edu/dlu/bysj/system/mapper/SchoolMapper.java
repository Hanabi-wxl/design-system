package edu.dlu.bysj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.School;
import edu.dlu.bysj.base.model.vo.SchoolSimplifyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/17 22:43
 */
@Repository
public interface SchoolMapper extends BaseMapper<School> {
  /**
   * 查询所有学校的id和 name
   *
   * @return
   */
  List<SchoolSimplifyVo> selectSchool();
}
