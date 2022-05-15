package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/10/8 15:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "查看中期检查表详情返回类")
public class MiddleCheckDetailVo {
    @ApiModelProperty(value = "文献完成数量")
    private Integer literatureQuantity;
    @ApiModelProperty(value = "初稿完成日期")
    private LocalDate finishDate;
    @ApiModelProperty(value = "工作量")
    private Integer workLoad;
    @ApiModelProperty(value = "难易度")
    private Integer difficulty;
    @ApiModelProperty(value = "出勤状况")
    private Integer attitude;
    @ApiModelProperty(value = "是否有任务书")
    private Integer hasTaskbook;
    @ApiModelProperty(value = "是否有开题报告")
    private Integer hasOpenreport;
    @ApiModelProperty(value = "工作进度")
    private Integer workingProgress;
    @ApiModelProperty(value = "中期工作结论")
    private Integer conclude;
    @ApiModelProperty(value = "调整情况")
    private String arrange;
}
