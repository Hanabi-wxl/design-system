package edu.dlu.bysj.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Degree;
import edu.dlu.bysj.base.model.vo.DegreeSimplifyVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:02
 */
public interface DegreeService extends IService<Degree> {
    /**
     * 查询所有学位信息
     *
     * @return 学位信息集合
     */
    List<DegreeSimplifyVo> allDegreeSimplifyInfo();
}
