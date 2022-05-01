package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/10/9 15:07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "日志返回类")
public class LogVo {
    @ApiModelProperty(value = "日期")
    private LocalDate date;
    @ApiModelProperty(value = "用户姓名")
    private String userName;
    @ApiModelProperty(value = "日志类型（0：操作日志，1：错误日志，2：登录日志）")
    private Integer type;
    @ApiModelProperty(value = "日志内容")
    private String message;
    @ApiModelProperty(value = "ip地址")
    private Integer ip;

}
