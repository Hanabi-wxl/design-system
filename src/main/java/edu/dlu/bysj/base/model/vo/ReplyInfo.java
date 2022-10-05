package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/11/8 11:00
 */
@Data
@ApiModel(description = "答辩信息返会类")
public class ReplyInfo {

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

}
