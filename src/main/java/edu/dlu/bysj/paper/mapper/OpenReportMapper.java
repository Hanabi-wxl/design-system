package edu.dlu.bysj.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.OpenReport;
import edu.dlu.bysj.document.entity.dto.OpenReportBaseInfo;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/3 11:24
 */
@Repository
public interface OpenReportMapper extends BaseMapper<OpenReport> {
    OpenReportBaseInfo getFileBaseInfoById(String subjectId);
}
