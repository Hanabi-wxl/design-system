package edu.dlu.bysj.paper.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.SubjectType;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.paper.mapper.SubjectTypeMapper;
import edu.dlu.bysj.paper.service.SubjectTypeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/28 22:09
 */
@Service
public class SubjectTypeServiceImpl extends ServiceImpl<SubjectTypeMapper, SubjectType> implements SubjectTypeService {


    private final RedisTemplate<String, Object> redisTemplate;


    public SubjectTypeServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Map<String, Object>> subjectTypeInfo() {
        List<SubjectType> list = null;
        List<Map<String, Object>> result;
        String key = RedisKeyEnum.SUBJECT_TYPE_NAME_ID_KEY.getKeyValue();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Long size = redisTemplate.boundListOps(key).size();
            if (ObjectUtil.isNotNull(size)) {
                Object value = redisTemplate.boundListOps(key).range(0, size).get(0);

                list = (List<SubjectType>) value;
            }
        } else {
            /*查询所有值*/
            list = this.list();
            if (list != null && !list.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(list);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }
        }

        /*对数据进行清洗*/
        result = this.clearSubjectType(list);

        return result;
    }

    private List<Map<String, Object>> clearSubjectType(List<SubjectType> source) {
        if (source == null) {
            return null;
        }

        List<Map<String, Object>> value = new ArrayList<>();
        for (SubjectType element : source) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("paperTypeId", element.getId());
            map.put("paperTypeName", element.getName());
            value.add(map);
        }
        return value;
    }
}
