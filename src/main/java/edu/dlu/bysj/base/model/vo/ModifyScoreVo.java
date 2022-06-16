package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/8 20:17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "修改总评成绩")
public class ModifyScoreVo {
    @ApiModelProperty(value = "题目Id")
    @NotNull
    private Integer subjectId;

    @ApiModelProperty(value = "总评评价-自述总结")
    @NotNull
    private Integer summary;

    @ApiModelProperty(value = "总评评价-答辩过程")
    @NotNull
    private Integer process;

    @ApiModelProperty(value = "选题质量")
    @NotNull
    private Integer quality;

    @ApiModelProperty(value = "完成质量")
    @NotNull
    private Integer completeQuality;
    @ApiModelProperty(value = "能力水平")
    @NotNull
    private Integer ability;

    @ApiModelProperty(value = "能力水平")
    private String comment;

    @ApiModelProperty(value = "是否二答")
    private Integer isSecond;
}
