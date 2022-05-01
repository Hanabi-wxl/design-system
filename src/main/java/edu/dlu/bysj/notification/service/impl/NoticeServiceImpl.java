package edu.dlu.bysj.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Notice;
import edu.dlu.bysj.notification.mapper.NoticeMapper;
import edu.dlu.bysj.notification.service.NoticeService;
import org.springframework.stereotype.Service;

/**
 * @author XiangXinGang
 * @date 2021/11/8 20:43
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}
