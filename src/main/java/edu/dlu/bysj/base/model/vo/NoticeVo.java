package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "通知列表返回类")
public class NoticeVo {

    @ApiModelProperty(value = "消息名称")
    private String noticeName;
    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;
    @ApiModelProperty(value = "发布单位(通知类型（0：校，1：学院通知，2：专业通知）)")
    private String unit;
    @ApiModelProperty(value = "消息id")
    private Integer noticeId;
    @ApiModelProperty(value = "通知状态（0：置顶，1：普通，2：隐藏）")
    private String importance;
    @ApiModelProperty(value = "通知状态（0：置顶，1：普通，2：隐藏）")
    private Integer type;
    @ApiModelProperty(value = "通知状态（0：置顶，1：普通，2：隐藏）")
    private String typeName;
    @ApiModelProperty(value = "消息内容")
    private String content;
    @ApiModelProperty(value = "学院id")
    private Integer collegeId;
    @ApiModelProperty(value = "专业id")
    private Integer majorId;
}
