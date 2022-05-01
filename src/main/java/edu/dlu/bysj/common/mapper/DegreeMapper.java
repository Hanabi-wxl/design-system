package edu.dlu.bysj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Degree;
import edu.dlu.bysj.base.model.vo.DegreeSimplifyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:02
 */
@Repository
public interface DegreeMapper extends BaseMapper<Degree> {
  /**
   * 查询简易的学位信息, id , name
   *
   * @return List<DegreeSimplifyVo>
   */
  List<DegreeSimplifyVo> selectAllSimplifyDegree();
}
