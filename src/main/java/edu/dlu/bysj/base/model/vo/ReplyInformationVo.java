package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/8 22:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "学生查看分组信息返回类")
public class ReplyInformationVo {
    @ApiModelProperty(value = "分组编号")
    private Integer groupNumber;
    @ApiModelProperty(value = "答辩序号")
    private Integer serial;
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "答辩要求")
    private String requirement;
}
