package edu.dlu.bysj.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Class;
import edu.dlu.bysj.base.model.entity.College;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.query.basic.CommonPage;
import edu.dlu.bysj.base.model.vo.ClassSimplifyVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.system.mapper.ClassMapper;
import edu.dlu.bysj.system.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/22 16:05
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
  @Autowired private ClassMapper classMapper;
  @Autowired private RedisTemplate redisTemplate;

  @Override
  public List<ClassSimplifyVo> classSimplifyInfo(Integer majorId) {
    String key = RedisKeyEnum.CLASS_MAJOR_NAME_ID_KEY.getKeyValue() + majorId;
    List<ClassSimplifyVo> classSimplifyVos = null;
    if (redisTemplate.hasKey(key)) {
      Long size = redisTemplate.boundListOps(key).size();
      classSimplifyVos = (List<ClassSimplifyVo>) redisTemplate.boundListOps(key).range(0, size).get(0);
    } else {
      classSimplifyVos = classMapper.selectAllSimplifyClass(majorId);
      if (classSimplifyVos != null && !classSimplifyVos.isEmpty()) {
        redisTemplate.boundListOps(key).rightPushAll(classSimplifyVos);
        redisTemplate.expire(key, 10, TimeUnit.DAYS);
      }
    }

    return classSimplifyVos;
  }

}
