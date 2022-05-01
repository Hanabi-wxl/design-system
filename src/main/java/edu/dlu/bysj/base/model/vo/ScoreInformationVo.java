package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/8 16:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "获取分数列表返回值类")
public class ScoreInformationVo {
    @ApiModelProperty(value = "过程评价-工作态度")
    private Integer processAttitude;

    @ApiModelProperty(value = "过程评价-遵守记录")
    private Integer processDiscipline;

    @ApiModelProperty(value = "过程评价-开题报告")
    private Integer processReport;

    @ApiModelProperty(value = "过程评价-任务完成")
    private Integer processComplete;

    @ApiModelProperty(value = "过程评价-评语")
    private String processComment;

    @ApiModelProperty(value = "过程评价-查重成绩")
    private Integer processSimilar;

    @ApiModelProperty(value = "第一指导老师-选题质量")
    private Integer firstQuality;

    @ApiModelProperty(value = "第一指导老师-能力水平")
    private Integer firstAbility;

    @ApiModelProperty(value = "第一指导老师-完成质量")
    private Integer firstComplete;

    @ApiModelProperty(value = "指导教师-评语")
    private String firstComment;

    @ApiModelProperty(value = "互评老师-选题质量")
    private Integer otherQuality;

    @ApiModelProperty(value = "互评老师-能力水平")
    private Integer otherAbility;

    @ApiModelProperty(value = "互评老师-完成质量")
    private Integer otherComplete;

    @ApiModelProperty(value = "互评老师-评语")
    private String otherComment;

    @ApiModelProperty(value = "总评评价-自述总结")
    private Integer totalSelfSummary;

    @ApiModelProperty(value = "总评评价-答辩过程")
    private Integer totalProcess;

    @ApiModelProperty(value = "总评评价-选题质量")
    private Integer totalQuality;

    @ApiModelProperty(value = "总评评价-完成质量")
    private Integer totalCompleteQuality;

    @ApiModelProperty(value = "总评评价-能力水平")
    private Integer totalAbility;

    @ApiModelProperty(value = "总评评价-评语")
    private String totalComment;

}
