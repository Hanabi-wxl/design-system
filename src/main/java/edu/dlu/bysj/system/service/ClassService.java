package edu.dlu.bysj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Class;
import edu.dlu.bysj.base.model.query.basic.CommonPage;
import edu.dlu.bysj.base.model.vo.ClassSimplifyVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/22 16:05
 */
public interface ClassService extends IService<Class> {
  /**
   * 获取本专业 class.id, class.name
   *
   * @param majorId 专业id
   * @return
   */
  List<ClassSimplifyVo> classSimplifyInfo(Integer majorId);

}
