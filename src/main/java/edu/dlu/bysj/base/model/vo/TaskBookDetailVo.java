package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/8 14:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "查看任务书详情返回类")
public class TaskBookDetailVo {
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "开始时间")
    private LocalDate startTime;
    @ApiModelProperty(value = "结束时间")
    private LocalDate endTime;
    @ApiModelProperty(value = "选题依据")
    private String according;
    @ApiModelProperty(value = "论文要求")
    private String demand;
    @ApiModelProperty(value = "工作重点")
    private String emphasis;
    @ApiModelProperty(value = "文献目录")
    private String reference;
    @ApiModelProperty(value = "任务计划书内容")
    private List<ContentVo> contents;
}
