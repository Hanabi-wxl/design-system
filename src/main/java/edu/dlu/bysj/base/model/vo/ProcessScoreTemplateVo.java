package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/8 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "过程管理根据分数返回评语参数接收类")
public class ProcessScoreTemplateVo {
    @ApiModelProperty(value = "题目id")
    @NotNull
    private Integer subjectId;

    @ApiModelProperty(value = "过程评价-工作态度")
    @NotNull
    private Integer attitude;

    @ApiModelProperty(value = "过程评价-遵守记录")
    @NotNull
    private Integer discipline;

    @ApiModelProperty(value = "过程评价-开题报告")
    @NotNull
    private Integer report;

    @ApiModelProperty(value = "过程评价-任务完成")
    @NotNull
    private Integer complete;

    @ApiModelProperty(value = "过程评价-查重成绩")
    @NotNull
    private Integer similar;
}
