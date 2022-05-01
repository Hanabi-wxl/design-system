package edu.dlu.bysj.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.NoticeFile;
import edu.dlu.bysj.notification.mapper.NoticeFileMapper;
import edu.dlu.bysj.notification.service.NoticeFileService;
import org.springframework.stereotype.Service;

/**
 * @author XiangXinGang
 * @date 2021/11/8 21:49
 */
@Service
public class NoticeFileServiceImpl extends ServiceImpl<NoticeFileMapper, NoticeFile> implements NoticeFileService {
}
