package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/11/13 11:05
 */
@Data
@ApiModel(description = "提交过程评价参数接收类")
public class ScoreProcessVo {
    @ApiModelProperty(value = "题目id")
    @NotNull
    private Integer subjectId;

    @ApiModelProperty(value = "工作态度")
    @NotNull
    private Integer attitude;

    @ApiModelProperty(value = "遵守纪律")
    @NotNull
    private Integer discipline;

    @ApiModelProperty(value = "开题报告")
    @NotNull
    private Integer report;

    @ApiModelProperty(value = "任务完成")
    @NotNull
    private Integer complete;

//    @ApiModelProperty(value = "成绩查重")
//    @NotNull
//    private Integer similar;

    @ApiModelProperty(value = "过程评价内容")
    @NotBlank
    private String comment;
}
