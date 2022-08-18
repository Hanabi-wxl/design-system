package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Message对象", description="消息表")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "发送人id")
    private Integer senderId;

    @ApiModelProperty(value = "接收者id")
    private Integer receiverId;

    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送人是否删除(1:删除,0:未删除)")
    private Integer senderDelete;

    @ApiModelProperty(value = "接收人是否删除(1:删除, 0未删除)")
    private Integer receiverDelete;

    @ApiModelProperty(value = "阅读标志（1：未读，0：已读）")
    private Integer hasRead;

    @ApiModelProperty(value = "消息等级（0：一般消息，1：专业消息，2：院级消息，3：校级消息）")
    private Integer level;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;
}
