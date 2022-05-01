package edu.dlu.bysj.paper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Message;
import edu.dlu.bysj.base.model.vo.MessageVo;
import edu.dlu.bysj.base.model.vo.ReceiveMessageVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.paper.mapper.MessageMapper;
import edu.dlu.bysj.paper.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/3 11:08
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public boolean sendMessage(String title, String content, Integer senderId, Integer receiverId, Integer messageLevel) {

        boolean messageFlag = false;

        if (!StringUtils.isEmpty(content)) {
            Message messageVo = new Message();
            messageVo.setTitle(title);
            messageVo.setContent(content);
            messageVo.setSenderId(senderId);
            messageVo.setReceiverId(receiverId);
            messageVo.setSendTime(LocalDateTime.now());
            messageVo.setLevel(messageLevel);
            messageFlag = this.save(messageVo);
        }
        return messageFlag;
    }


    @Override
    public TotalPackageVo<MessageVo> messageListAsReviver(Integer reviverId, Integer start, Integer pageSize) {
        List<MessageVo> messageVos = baseMapper.selectMessageAsReviver(reviverId, start, pageSize);
        Integer total = baseMapper.totalMessageAsReviver(reviverId);
        TotalPackageVo<MessageVo> result = new TotalPackageVo<>();
        result.setTotal(total);
        result.setArrays(messageVos);
        return result;
    }

    @Override
    public TotalPackageVo<ReceiveMessageVo> messageListAsSender(Integer senderId, Integer start, Integer pageSize) {
        List<ReceiveMessageVo> receiveMessageVos = baseMapper.selectMessageAsSender(senderId, start, pageSize);
        int total = this.count(new QueryWrapper<Message>().eq("sender_id", senderId));
        TotalPackageVo<ReceiveMessageVo> result = new TotalPackageVo<>();
        result.setTotal(total);
        result.setArrays(receiveMessageVos);
        return result;
    }
}
