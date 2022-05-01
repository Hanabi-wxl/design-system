package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/10/8 21:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "获取答辩分组列表返回类")
public class ReplyTeamVo {
    @ApiModelProperty(value = "开始时间")
    private LocalDate startTime;
    @ApiModelProperty(value = "答辩地址")
    private String address;
    @ApiModelProperty(value = "要求")
    private String requirement;
    @ApiModelProperty(value = "结束时间")
    private LocalDate  endTime;
    @ApiModelProperty(value = "组号")
    private Integer groupNumber;
    @ApiModelProperty(value = "分组id")
    private Integer groupId;

}
