package edu.dlu.bysj.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Message;
import edu.dlu.bysj.base.model.vo.MessageVo;
import edu.dlu.bysj.base.model.vo.ReceiveMessageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/3 11:07
 */
@Repository
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 获取作为收件人的消息列表
     *
     * @param reviverId 收件人id
     * @param start     分页开始位置
     * @param pageSize  每页记录数
     * @return
     */
    List<MessageVo> selectMessageAsReviver(@Param("reviverId") Integer reviverId,
                                           @Param("start") Integer start,
                                           @Param("pageSize") Integer pageSize);

    /**
     * selectMessageAsReviver 的总数
     *
     * @param reviverId
     * @return
     */
    Integer totalMessageAsReviver(Integer reviverId);


    /**
     * 获取作为发件人消息列表
     *
     * @param senderId 发送者id
     * @param start    开始位置
     * @param pageSize 每页记录数
     * @return
     */

    List<ReceiveMessageVo> selectMessageAsSender(@Param("senderId") Integer senderId,
                                                 @Param("start") Integer start,
                                                 @Param("pageSize") Integer pageSize);

}
