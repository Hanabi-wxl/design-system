package edu.dlu.bysj.paper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.OpenReport;
import edu.dlu.bysj.paper.mapper.OpenReportMapper;
import edu.dlu.bysj.paper.service.OpenReportService;
import org.springframework.stereotype.Service;

/**
 * @author XiangXinGang
 * @date 2021/11/3 11:25
 */
@Service
public class OpenReportServiceImpl extends ServiceImpl<OpenReportMapper, OpenReport> implements OpenReportService {
}
