package edu.dlu.bysj.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.MessageFile;
import edu.dlu.bysj.notification.mapper.MessageFileMapper;
import edu.dlu.bysj.notification.service.MessageFileService;
import org.springframework.stereotype.Service;

/**
 * @author XiangXinGang
 * @date 2021/11/9 14:39
 */
@Service
public class MessageFileServiceImpl extends ServiceImpl<MessageFileMapper, MessageFile> implements MessageFileService {
}
