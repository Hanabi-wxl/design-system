package edu.dlu.bysj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.RoleFunction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/18 14:57
 */
@Repository
public interface RoleFunctionMapper extends BaseMapper<RoleFunction> {

    /**
     * 通过roleId 获取该角色下的所有functionIds
     * @param roleId 角色id
     * @return 功能id集合
     */
    List<Integer> getRoleFunctionIds(String roleId);
}
