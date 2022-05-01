package edu.dlu.bysj.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Role;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.RoleSimplifyVo;
import edu.dlu.bysj.system.mapper.RoleMapper;
import edu.dlu.bysj.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/18 9:19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<RoleSimplifyVo> roleInfo() {
        String key = RedisKeyEnum.ROLE_NAME_ID_KEY.getKeyValue();
        List<RoleSimplifyVo> roleSimplifyVoList = null;
        if (redisTemplate.hasKey(key)) {
            Long size = redisTemplate.boundListOps(key).size();
            roleSimplifyVoList = redisTemplate.boundListOps(key).range(0, size);
        } else {
            roleSimplifyVoList = roleMapper.selectAllRole();
            if (roleSimplifyVoList != null && !roleSimplifyVoList.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(roleSimplifyVoList);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }
        }
        roleMapper.selectAllRole();
        return null;
    }

    @Override
    public List<Integer> roleIds() {
        return roleMapper.selectAllRoleId();
    }

}
