package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/10/9 11:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "获取发件箱消息列表")
public class ReceiveMessageVo {
    @ApiModelProperty(value = "消息标题")
    private String messageTitle;
    @ApiModelProperty(value = "消息id")
    private Integer messageId;
    @ApiModelProperty(value = "接收人id")
    private Integer receiverId;
    @ApiModelProperty(value = "发送时间")
    private LocalDate  sendTime;
    @ApiModelProperty(value = "是否已读(1:已读,0未读)")
    private Integer hasRead;
    @ApiModelProperty(value = "接收人姓名")
    private String receiverName;
}
