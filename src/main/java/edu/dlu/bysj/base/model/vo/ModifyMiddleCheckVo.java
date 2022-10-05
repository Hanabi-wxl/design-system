package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/11/3 22:49
 */
@Data
@ApiModel(description = "提交/修改中期检查报告镀锡")
public class ModifyMiddleCheckVo {
    @ApiModelProperty(value = "题目id")
    @NotNull(message = "题目不能为空")
    private Integer subjectId;

    @ApiModelProperty(value = "难易程度,0表示偏难,1表示适中,2表示偏易")
    @NotNull(message = "难易度不能为空")
    private Integer difficulty;

    @ApiModelProperty(value = "文献数量")
    @NotNull(message = "文献量不能为空")
    private Integer literatureQuantity;


    @ApiModelProperty(value = "工作量,0表示较少,1表示适中,2表示较多")
    @NotNull(message = "工作量不能为空")
    private Integer workLoad;


    @ApiModelProperty(value = "初稿完成时间")
    @NotNull(message = "初稿完成时间不能为空")
    private LocalDate finishDate;

    @ApiModelProperty(value = "开题报告,0表示无,1表示有")
    @NotNull(message = "任务书不能为空")
    private Integer hasTaskBook;

    @ApiModelProperty(value = "开题报告,0表示无,1表示有")
    @NotNull(message = "开题报告不能为空")
    private Integer hasOpenReport;


    @ApiModelProperty(value = "学习态度,0表示好,1表示一般,2表示差")
    @NotNull(message = "学习态度不能为空")
    private Integer attitude;


    @ApiModelProperty(value = "工作进度,0表示快,1表示按计划进行,2表示慢")
    @NotNull(message = "工作进度不能为空")
    private Integer workingProgress;

    @ApiModelProperty(value = "中期工作结论,0表示优,1表示良,2表示中,3表示差")
    @NotNull(message = "中期工作结论不能为空")
    private Integer conclude;

    @ApiModelProperty(value = "调整情况")
    private String arrange;


}
