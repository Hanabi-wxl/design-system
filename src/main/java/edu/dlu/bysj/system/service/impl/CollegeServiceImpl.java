package edu.dlu.bysj.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.College;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.query.basic.CommonPage;
import edu.dlu.bysj.base.model.vo.CollegeSimpleInoVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.system.mapper.CollegeMapper;
import edu.dlu.bysj.system.service.CollegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/18 16:40
 */
@Service
@Slf4j
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College>
        implements CollegeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CollegeMapper collegeMapper;

    @Override
    public TotalPackageVo<College> collegePagination(CommonPage page, Integer schoolId) {

        List<College> result = null;
        String zsetKey = RedisKeyEnum.COLLEGE_ZSET_KEY.getKeyValue() + schoolId;
        String hashKey = RedisKeyEnum.COLLEGE_HASH_KEY.getKeyValue() + schoolId;
        // 分页再[start, end-1]
        Integer start = (page.getPageNumber() - 1) * page.getPageSize();
        Integer end = (page.getPageNumber()) * page.getPageSize() - 1;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(zsetKey)) && Boolean.TRUE.equals(redisTemplate.hasKey(hashKey))) {
            result = this.getPage(zsetKey, hashKey, start, end);
        } else {
            // 查询学院根据学校id值;
            List<College> list = this.list(new QueryWrapper<College>().eq("school_id", schoolId));
            for (College element : list) {
                // 对于hkey 进行转换
                String hkey = String.valueOf(element.getId());
                Double score = Double.valueOf(element.getId());
                this.setPage(zsetKey, hashKey, hkey, score, element);
            }
            // 分页;
            result = this.getPage(zsetKey, hashKey, start, end);
        }
        TotalPackageVo<College> packageVo = new TotalPackageVo<>();
        packageVo.setArrays(result);
        Long total = redisTemplate.opsForZSet().size(zsetKey);

        if (ObjectUtil.isNotNull(total)) {
            packageVo.setTotal(total.intValue());
        }

        return packageVo;
    }

    @Override
    public College findCollegeObject(College college) {
        return collegeMapper.getCollegeByCondition(college);
    }


    @Override
    public List<CollegeSimpleInoVo> obtainCollegeInfoBySchool(Integer schoolId) {
        List<CollegeSimpleInoVo> result = null;
        String key = RedisKeyEnum.COLLEGE_NAME_ID_KEY.getKeyValue() + schoolId;
        if (Boolean.TRUE.equals(key)) {
            Long size = redisTemplate.boundListOps(key).size();
            if (ObjectUtil.isNotNull(size)) {
                result = redisTemplate.boundListOps(key).range(0, size);
            }
        } else {
            result = collegeMapper.getCollegeBySchoolId(schoolId);
            if (result != null && !result.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(result);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }
        }

        return result;
    }

    /*
     * @Description:  根据专业id获取学院id
     * @Author: sinre
     * @Date: 2022/5/2 11:07
     * @param majorId
     * @return java.lang.Integer
     **/
    @Override
    public Integer getCollegeIdByMajorId(Integer majorId) {
        return collegeMapper.getCollegeId(majorId);
    }

    /**
     * 向 redis hash 中存放单个数据
     *
     * @param key   hashMap key
     * @param hkey  hashMap 映射key;
     * @param value 存放对象
     * @return 操作是否成功
     */
    private boolean storeSingleHash(String key, String hkey, College value) {
        try {
            redisTemplate.opsForHash().put(key, hkey, value);
            return true;
        } catch (Exception e) {
            log.warn("hput {} ={}", key + key, value);
        }
        return false;
    }

    /**
     * 向zset集合存值
     *
     * @param zsetKey   zset key
     * @param hashKey   hash key
     * @param hkey      zset 映射key
     * @param collegeId 学院id
     * @param value     college对象
     * @return
     */
    private boolean setPage(
            String zsetKey, String hashKey, String hkey, double collegeId, College value) {
        boolean result = false;
        try {
            redisTemplate.opsForZSet().add(zsetKey, hkey, collegeId);
            result = storeSingleHash(hashKey, hkey, value);
        } catch (Exception e) {
            log.warn("setPage = {}", zsetKey);
        }
        return result;
    }

    private List<College> getPage(String zsetKey, String hashKey, Integer start, Integer end) {
        List<College> result = new ArrayList<>();
        Set<String> set = redisTemplate.opsForZSet().range(zsetKey, start, end);

        for (Object element : set) {
            College college = ((College) redisTemplate.opsForHash().get(hashKey, element));
            result.add(college);
        }
        return result;
    }
}
