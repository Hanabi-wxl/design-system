package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/9 11:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "接收发送消息参数对象")
public class AddMessageVo {
    @ApiModelProperty(value = "消息标题")
    @NotBlank
    private String messageTitle;

    @ApiModelProperty(value = "消息内容")
    @NotBlank
    private String messageContent;

    @ApiModelProperty(value = "学号")
    @NotBlank
    private String receiver;

    @ApiModelProperty(value = "文件ids")
    private List<Integer> fileId;

    @ApiModelProperty(value = "消息的等级")
    @NotNull
    private Integer level;
}
