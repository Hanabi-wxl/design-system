package edu.dlu.bysj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Role;
import edu.dlu.bysj.base.model.vo.RoleSimplifyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/18 9:18
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询所有角色的Id, name
     *
     * @return id, name 集合
     */
    List<RoleSimplifyVo> selectAllRole();


    /**
     * 查询所有的roleId
     *
     * @return roleid，集合
     */
    List<Integer> selectAllRoleId();

    
}
