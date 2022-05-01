package edu.dlu.bysj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Role;
import edu.dlu.bysj.base.model.vo.RoleSimplifyVo;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/18 9:19
 */
public interface RoleService extends IService<Role> {
    /**
     * 获取所有 role的id ,name
     *
     * @return List<RoleSimplifyVo>
     */
    List<RoleSimplifyVo> roleInfo();

    /**
     * 获取所有roleIds
     *
     * @return ids 集合
     */
    List<Integer> roleIds();

}
