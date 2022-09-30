package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/11/7 9:47
 */
@Data
@ApiModel(description = "分组信息返回类")
public class TeamInfoVo {
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm ss", timezone = "Asia/Shanghai")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm ss", timezone = "Asia/Shanghai")
    private LocalDateTime endTime;

    private String rangeTime;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "要求")
    private String require;

    @ApiModelProperty(value = "分组编号")
    private String teamNumber;

    @ApiModelProperty(value = "组号id")
    private Integer groupId;
}
