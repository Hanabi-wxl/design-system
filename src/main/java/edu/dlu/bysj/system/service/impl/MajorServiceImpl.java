package edu.dlu.bysj.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.enums.RedisKeyEnum;
import edu.dlu.bysj.base.model.vo.MajorSimpleInfoVo;
import edu.dlu.bysj.base.model.vo.MajorVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.model.vo.UserVo;
import edu.dlu.bysj.system.mapper.MajorMapper;
import edu.dlu.bysj.system.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangXinGang
 * @date 2021/10/21 20:37
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MajorMapper majorMapper;

    @Override
    public TotalPackageVo<MajorVo> majorPagination(
            String collegeId, Integer pageNumber, Integer pagesize) {

        String zsetKey = RedisKeyEnum.MAJOR_ZSET_KEY.getKeyValue() + collegeId;
        String hashKey = RedisKeyEnum.MAJOR_HASH_KEY.getKeyValue() + collegeId;
        List<MajorVo> result;
        if (redisTemplate.hasKey(zsetKey) && redisTemplate.hasKey(hashKey)) {
            result = this.getPage(redisTemplate, zsetKey, hashKey, pageNumber, pagesize);
        } else {
            List<MajorVo> majorVos = majorMapper.selectMajorList(collegeId);

            // 向缓存中存放数据;
            for (MajorVo element : majorVos) {
                redisTemplate.opsForHash().put(hashKey, String.valueOf(element.getMajorId()), element);
                redisTemplate
                        .opsForZSet()
                        .add(zsetKey, String.valueOf(element.getMajorId()), Double.valueOf(element.getMajorId()));
            }

            redisTemplate.expire(zsetKey, 10, TimeUnit.DAYS);
            redisTemplate.expire(hashKey, 10, TimeUnit.DAYS);
            // 从redisTemplate 中获取分页列表;
            result = this.getPage(redisTemplate, zsetKey, hashKey, pageNumber, pagesize);
        }
        // 获取长度
        Long size = redisTemplate.opsForZSet().zCard(zsetKey);

        TotalPackageVo<MajorVo> totalPackageVo = new TotalPackageVo<>();
        if (ObjectUtil.isNotNull(size)) {
            totalPackageVo.setTotal(size.intValue());
        }
        totalPackageVo.setArrays(result);
        return totalPackageVo;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean generateFillingNumber(Integer majorId, Integer year) {
        /*按序归档序的题目id*/
        List<Integer> list = majorMapper.archiveNumber(majorId, year);
        /*归档序号从1开始*/
        for (int i = 0; i < list.size(); i++) {
            majorMapper.updateFillNumber(list.get(i), i + 1);
        }
        return true;
    }


    @Override
    public List<MajorSimpleInfoVo> obtainCollegeMajor(Integer collegeId) {
        String key = RedisKeyEnum.COLLEGE_MAJOR_SIMPLIFY_INFO_KEY.getKeyValue() + collegeId;
        List<MajorSimpleInfoVo> result = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Long size = redisTemplate.boundListOps(key).size();
            if (ObjectUtil.isNotNull(size)) {
                Object range = redisTemplate.boundListOps(key).range(0, size);
                result = (List<MajorSimpleInfoVo>) range;
            }
        } else {
            /*查询*/
            result = majorMapper.selectMajorInfoByCollegeId(collegeId);

            if (result != null && !result.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(result);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }
        }
        return result;
    }

    @Override
    public List<UserVo> obtainCollegeTeacher(Integer collegeId) {
        String key = RedisKeyEnum.COLLEGE_TEACHER_KEY.getKeyValue() + collegeId;
        List<UserVo> result = null;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Long size = redisTemplate.boundListOps(key).size();
            if (ObjectUtil.isNotNull(size)) {
                Object range = redisTemplate.boundListOps(key).range(0, size).get(0);
                result = (List<UserVo>) range;
            }
        } else {
            /*查询*/
            result = majorMapper.obtainCollegeTeacher(collegeId);

            if (result != null && !result.isEmpty()) {
                redisTemplate.boundListOps(key).rightPushAll(result);
                redisTemplate.expire(key, 10, TimeUnit.DAYS);
            }
        }
        return result;
    }


    private List<MajorVo> getPage(
            RedisTemplate redisTemplate,
            String zsetkey,
            String hashKey,
            Integer pageNumber,
            Integer pagesize) {
        // 获取zset中的value
        List<MajorVo> result = new ArrayList<>();
        Set<String> range =
                redisTemplate
                        .opsForZSet()
                        .range(zsetkey, (pageNumber - 1) * pagesize, pageNumber * pagesize - 1);

        for (String element : range) {
            MajorVo majorVo = (MajorVo) redisTemplate.opsForHash().get(hashKey, element);
            result.add(majorVo);
        }
        return result;
    }
}
