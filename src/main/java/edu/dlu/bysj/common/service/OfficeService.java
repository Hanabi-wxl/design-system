package edu.dlu.bysj.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Office;
import edu.dlu.bysj.base.model.vo.OfficeSimplifyVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:32
 */
public interface OfficeService extends IService<Office> {
  /**
   * 查询所有科室的id,name
   *
   * @return List<OfficeSimplifyVo>
   */
  List<OfficeSimplifyVo> officeSimplifyInfo();
}
