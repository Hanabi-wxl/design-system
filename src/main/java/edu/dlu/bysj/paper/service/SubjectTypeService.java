package edu.dlu.bysj.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.SubjectType;

import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/10/28 22:08
 */
public interface SubjectTypeService extends IService<SubjectType> {
    /**
     * 查询所有的typeInfo;
     *
     * @return id, name 组成的map 嵌套list
     */
    List<Map<String, Object>> subjectTypeInfo();
}
