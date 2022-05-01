package edu.dlu.bysj.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.RoleFunction;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.system.mapper.RoleFunctionMapper;
import edu.dlu.bysj.system.service.RoleFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/18 14:59
 */
@Service
public class RoleFunctionServiceImpl extends ServiceImpl<RoleFunctionMapper, RoleFunction> implements RoleFunctionService {
    @Autowired
    private RoleFunctionMapper roleFunctionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Integer> ObtainFunctionIds(String roleId) {
        List<Integer> result = null;
        String key = RedisKeyEnum.ROLE_FUNCTION_KEY.getKeyValue() +roleId;
        if (redisTemplate.hasKey(key)) {
            // [0,-1] 返回所有值;
            result = redisTemplate.opsForList().range(key,0,-1);
        } else {
            result = roleFunctionMapper.getRoleFunctionIds(roleId);
            //放入redis中
            redisTemplate.opsForList().rightPushAll(key,result);
            //设置10天有效时间
            redisTemplate.expire(key,10, TimeUnit.DAYS);
        }
        return result;
    }
}
