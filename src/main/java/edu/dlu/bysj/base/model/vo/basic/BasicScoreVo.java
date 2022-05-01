package edu.dlu.bysj.base.model.vo.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/11/10 11:21
 */
@Data
@ApiModel(description = "通用成绩评价提交类")
public class BasicScoreVo {
    @ApiModelProperty(value = "选题质量")
    @NotNull
    private Integer quality;

    @ApiModelProperty(value = "能力水平")
    @NotNull
    private Integer ability;

    @ApiModelProperty(value = "完成质量")
    @NotNull
    private Integer complete;

    @ApiModelProperty(value = "论文题目id")
    @NotNull
    private Integer subjectId;

    @ApiModelProperty(value = "评语")
    @NotBlank
    private String comment;

}
