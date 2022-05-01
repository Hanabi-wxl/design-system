package edu.dlu.bysj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.School;
import edu.dlu.bysj.base.model.vo.SchoolSimplifyVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/17 22:44
 */
public interface SchoolService extends IService<School> {
  /**
   * 查询学校id，name
   *
   * @return List<SchoolSimplifyVo>
   */
  List<SchoolSimplifyVo> schoolInfo();
}
