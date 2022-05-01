package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/8 16:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "获取成绩汇总表返回值")
public class ScoreSummaryVo {
    @ApiModelProperty(value = "指导教师评价-优秀百分比")
    private Float firstOutstanding;
    @ApiModelProperty(value = "过程管理评价-优秀百分比")
    private Float processOutstanding;
    @ApiModelProperty(value = "互评评价-优秀百分比")
    private Float otherOutstanding;
    @ApiModelProperty(value = "答辩成绩-优秀百分比")
    private Float totalOutstanding;
    @ApiModelProperty(value = "指导教师评价-良好百分比")
    private Float firstGood;
    @ApiModelProperty(value = "过程管理评价-良好百分比")
    private Float processGood;
    @ApiModelProperty(value = "互评评价-良好百分比")
    private Float otherGood;
    @ApiModelProperty(value = "答辩成绩-良好百分比")
    private Float totalGood;
    @ApiModelProperty(value = "指导教师评价-中等百分比")
    private Float firstMiddle;
    @ApiModelProperty(value = "过程管理评价-中等百分比")
    private Float processMiddle;
    @ApiModelProperty(value = "互评评价-中等百分比")
    private Float otherMiddle;
    @ApiModelProperty(value = "答辩成绩-中等百分比")
    private Float totalMiddle;
    @ApiModelProperty(value = "指导教师评价-较差百分比")
    private Float firstBad;
    @ApiModelProperty(value = "过程管理评价-较差百分比")
    private Float processBad;
    @ApiModelProperty(value = "互评评价-较差百分比")
    private Float otherBad;
    @ApiModelProperty(value = "答辩成绩-较差百分比")
    private Float totalBad;
    @ApiModelProperty(value = "指导教师评价-不及格百分比")
    private Float firstFailed;
    @ApiModelProperty(value = "过程管理评价-不及格百分比")
    private Float processFailed;
    @ApiModelProperty(value = "互评评价-不及格百分比")
    private Float otherFailed;
    @ApiModelProperty(value = "答辩成绩-不及格百分比")
    private Float totalFailed;
    @ApiModelProperty(value = "指导教师评价-未打分百分比")
    private Float firstNoScore;
    @ApiModelProperty(value = "过程管理评价-未打分百分比")
    private Float processNoScore;
    @ApiModelProperty(value = "互评评价-未打分百分比")
    private Float otherNoScore;
    @ApiModelProperty(value = "答辩成绩-未打分百分比")
    private Float totalNoScore;
}
