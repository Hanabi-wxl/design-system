package edu.dlu.bysj.notification.service;

import edu.dlu.bysj.base.model.entity.Notice;
import edu.dlu.bysj.base.model.vo.NoticeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/8 20:43
 */
public interface NoticeService extends IService<Notice> {

    List<NoticeVo> allNoticeList (Integer collegeId, Integer majorId);
}
