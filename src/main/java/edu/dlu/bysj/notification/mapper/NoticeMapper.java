package edu.dlu.bysj.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Notice;
import edu.dlu.bysj.base.model.vo.NoticeVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/8 20:42
 */
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {
    List<NoticeVo> allNoticeList(Integer majorId, Integer collegeId);
}
