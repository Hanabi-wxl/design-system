package edu.dlu.bysj.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Message;
import edu.dlu.bysj.base.model.vo.MessageVo;
import edu.dlu.bysj.base.model.vo.ReceiveMessageVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;

/**
 * @author XiangXinGang
 * @date 2021/11/3 11:08
 */

public interface MessageService extends IService<Message> {
    /**
     * 发送消息
     *
     * @param title        消息标题
     * @param content      消息内容
     * @param senderId     消息发送者id
     * @param receiverId   消息接收者id
     * @param messageLevel 消息级别
     * @return 是否发送成功
     */
    boolean sendMessage(String title, String content, Integer senderId, Integer receiverId, Integer messageLevel);


    /**
     * 接收消息列表
     *
     * @param reviverId 接收者id
     * @param start     分页起始位置
     * @param pageSize  每页记录数
     * @return 分页的MessageVo集合
     */
    TotalPackageVo<MessageVo> messageListAsReviver(Integer reviverId, Integer start, Integer pageSize);

    /**
     * 获取发送消息列表
     *
     * @param senderId 发送人id
     * @param start    分页起始位置
     * @param end      分页结束位置
     * @return
     */
    TotalPackageVo<ReceiveMessageVo> messageListAsSender(Integer senderId, Integer start, Integer end);
}
