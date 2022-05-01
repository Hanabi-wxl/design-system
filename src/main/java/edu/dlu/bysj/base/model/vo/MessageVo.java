package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "收件箱消息列表返回类")
public class MessageVo {
    @ApiModelProperty(value = "消息标题")
    private String messageTitle;
    @ApiModelProperty(value = "消息id")
    private Integer messageId;
    @ApiModelProperty(value = "发送人姓名")
    private String senderName;
    @ApiModelProperty(value = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime sendTime;
    @ApiModelProperty(value = "是否以读")
    private Integer hasRead;
}
