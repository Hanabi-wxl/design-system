package edu.dlu.bysj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Class;
import edu.dlu.bysj.base.model.vo.ClassSimplifyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/22 16:04
 */
@Repository
public interface ClassMapper extends BaseMapper<Class> {
  /**
   * 查询本专业所有的class.id, class.name
   *
   * @param majorId :专业id
   * @return List<ClassSimplifyVo>
   */
  List<ClassSimplifyVo> selectAllSimplifyClass(Integer majorId);
}
