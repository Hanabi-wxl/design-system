package edu.dlu.bysj.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Source;
import edu.dlu.bysj.base.model.vo.SubjectSourceVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/28 20:10
 */

public interface SourceService extends IService<Source> {
    /**
     * 获取所有的source();
     *
     * @return 封装, sourcerId, sourceName,的集合;
     */
    List<SubjectSourceVo> checkAllSourceInfo();
}
