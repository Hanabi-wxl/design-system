package edu.dlu.bysj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Office;
import edu.dlu.bysj.base.model.vo.OfficeSimplifyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:26
 */
@Repository
public interface OfficeMapper extends BaseMapper<Office> {
  /**
   * 查询所有的office,id,name;
   *
   * @return List<OfficeSimplifyVo>
   */
  List<OfficeSimplifyVo> selectAllSimplifyOffice();
}
