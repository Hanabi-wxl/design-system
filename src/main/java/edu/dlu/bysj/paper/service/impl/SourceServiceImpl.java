package edu.dlu.bysj.paper.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Source;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.SubjectSourceVo;
import edu.dlu.bysj.paper.mapper.SourceMapper;
import edu.dlu.bysj.paper.service.SourceService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/28 20:10
 */
@Service
public class SourceServiceImpl extends ServiceImpl<SourceMapper, Source> implements SourceService {
    private final SourceMapper sourceMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public SourceServiceImpl(SourceMapper sourceMapper, RedisTemplate<String, Object> redisTemplate) {
        this.sourceMapper = sourceMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<SubjectSourceVo> checkAllSourceInfo() {
        String key = RedisKeyEnum.SOURCE_NAME_ID_KEY.getKeyValue();
        List<SubjectSourceVo> result = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Long size = redisTemplate.boundListOps(key).size();
            if (ObjectUtil.isNotNull(size)) {
                Object range = redisTemplate.boundListOps(key).range(0, size).get(0);
                result = (List<SubjectSourceVo>) range;
            }
        } else {
            result = sourceMapper.selectAllSource();
            if (result != null && !result.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(result);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }
        }
        return result;
    }
}
