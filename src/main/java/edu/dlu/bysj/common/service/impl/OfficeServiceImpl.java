package edu.dlu.bysj.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Office;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.OfficeSimplifyVo;
import edu.dlu.bysj.common.mapper.OfficeMapper;
import edu.dlu.bysj.common.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:32
 */
@Service
public class OfficeServiceImpl extends ServiceImpl<OfficeMapper, Office> implements OfficeService {
  @Autowired private OfficeMapper officeMapper;
  @Autowired private RedisTemplate redisTemplate;

  @Override
  public List<OfficeSimplifyVo> officeSimplifyInfo() {
    String key = RedisKeyEnum.OFFICE_NAME_ID_KEY.getKeyValue();
    List<OfficeSimplifyVo> result = null;
    if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
      Long size = redisTemplate.boundListOps(key).size();
      result = (List<OfficeSimplifyVo>) redisTemplate.boundListOps(key).range(0, size).get(0);
    } else {
      result = officeMapper.selectAllSimplifyOffice();
      if (result != null && !result.isEmpty()) {
        redisTemplate.boundListOps(key).rightPushAll(result);
        redisTemplate.expire(key, 10, TimeUnit.DAYS);
      }
    }
    return result;
  }
}
