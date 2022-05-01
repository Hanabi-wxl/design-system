package edu.dlu.bysj.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.School;
import edu.dlu.bysj.base.model.vo.SchoolSimplifyVo;
import edu.dlu.bysj.system.mapper.SchoolMapper;
import edu.dlu.bysj.system.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/17 22:48
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
    private final SchoolMapper schoolMapper;

    @Autowired
    public SchoolServiceImpl(SchoolMapper schoolMapper) {
        this.schoolMapper = schoolMapper;
    }

    @Override
    public List<SchoolSimplifyVo> schoolInfo() {

        return schoolMapper.selectSchool();
    }
}
