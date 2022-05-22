package edu.dlu.bysj.defense.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Progress;
import edu.dlu.bysj.defense.mapper.ProgressMapper;
import edu.dlu.bysj.defense.service.ProgressService;
import org.springframework.stereotype.Service;

/**
 * @author sinre
 * @create 05 19, 2022
 * @since 1.0.0
 */
@Service
public class ProgressServiceImpl extends ServiceImpl<ProgressMapper, Progress> implements ProgressService {
}
