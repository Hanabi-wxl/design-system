package edu.dlu.bysj.defense.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.TeamConfig;
import edu.dlu.bysj.defense.mapper.TeamConfigMapper;
import edu.dlu.bysj.defense.service.TeamConfigService;
import org.springframework.stereotype.Service;

/**
 * @author XiangXinGang
 * @date 2021/11/7 14:59
 */
@Service
public class TeamConfigServiceImpl extends ServiceImpl<TeamConfigMapper, TeamConfig> implements TeamConfigService {
}
