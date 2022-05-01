package edu.dlu.bysj.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Function;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.FunctionSimplifyVo;
import edu.dlu.bysj.base.model.vo.FunctionTimeVo;
import edu.dlu.bysj.system.mapper.FunctionMapper;
import edu.dlu.bysj.system.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/15 20:39
 */
@Service
public class FunctionServiceImpl extends ServiceImpl<FunctionMapper, Function>
        implements FunctionService {

    private final FunctionMapper functionMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    /*采用构造器注入*/
    @Autowired
    public FunctionServiceImpl(FunctionMapper functionMapper, RedisTemplate<String, Object> redisTemplate) {
        this.functionMapper = functionMapper;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean updateFunctionTime(
            Integer functionId, LocalDateTime startTime, LocalDateTime endTime) {

        return functionMapper.updateStartTimeAndEndTime(functionId, startTime, endTime) == 1;
    }

    @Override
    public List<FunctionSimplifyVo> personFunctionByTime(List<Integer> role, String currentTime) {
        return functionMapper.getPersonFunction(role, currentTime);
    }

    @Override
    public List<FunctionTimeVo> allFunction(Integer majorId) {
        String key = RedisKeyEnum.MAJOR_FUNCTION_TABLE_KEY.getKeyValue() + majorId;
        List<FunctionTimeVo> result = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Long size = redisTemplate.boundListOps(key).size();
            if (ObjectUtil.isNotNull(size)) {
                Object range = redisTemplate.boundListOps(key).range(0, size);
                result = (List<FunctionTimeVo>) range;
            }
        } else {
            result = functionMapper.getFunctionByMajorId(majorId);

            if (result != null && !result.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(result);
            }
        }
        return result;
    }
}
