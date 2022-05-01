package edu.dlu.bysj.base.model.vo;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/8 20:36
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "答辩成绩模板参数接收类")
public class TotalScoreTemplateVo {
    @ApiModelProperty(value = "总评评价-自述总结")
    @NotNull
    private Integer summary;

    @ApiModelProperty(value = "总评评价-答辩过程")
    @NotNull
    private Integer process;

    @ApiModelProperty(value = "总评评价-选题质量")
    @NotNull
    private Integer quality;

    @ApiModelProperty(value = "总评评价-完成质量")
    @NotNull
    private Integer completeQuality;

    @ApiModelProperty(value = "总评评价-能力水平")
    @NotNull
    private Integer ability;

    @ApiModelProperty(value = "题目id")
    @NotNull
    private Integer subjectId;

}
