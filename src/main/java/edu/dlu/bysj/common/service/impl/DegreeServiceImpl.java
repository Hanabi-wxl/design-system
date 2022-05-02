package edu.dlu.bysj.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Degree;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.DegreeSimplifyVo;
import edu.dlu.bysj.common.mapper.DegreeMapper;
import edu.dlu.bysj.common.service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:08
 */
@Service
public class DegreeServiceImpl extends ServiceImpl<DegreeMapper, Degree> implements DegreeService {
    @Autowired
    private DegreeMapper degreeMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<DegreeSimplifyVo> allDegreeSimplifyInfo() {
        String key = RedisKeyEnum.DEGREE_NAME_ID_KEY.getKeyValue();
        List<DegreeSimplifyVo> result = null;
        degreeMapper.selectAllSimplifyDegree();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Long size = redisTemplate.boundListOps(key).size();
            result = (List<DegreeSimplifyVo>) redisTemplate.opsForList().range(key, 0, size).get(0);
        } else {
            result = degreeMapper.selectAllSimplifyDegree();
            if (result != null && !result.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(result);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }
        }
        return result;
    }

    
}
