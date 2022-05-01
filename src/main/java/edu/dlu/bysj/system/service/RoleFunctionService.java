package edu.dlu.bysj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.RoleFunction;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/18 14:59
 */
public interface RoleFunctionService  extends IService<RoleFunction> {
    /**
     * 通过roleId 获取functionIds;
     * @param roleId 角色id
     * @return 功能更集合;
     */
    List<Integer> ObtainFunctionIds(String roleId);
}
