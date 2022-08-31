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
    private Integer quality;

    @ApiModelProperty(value = "能力水平")
    private Integer ability;

    @ApiModelProperty(value = "完成质量")
    private Integer complete;

    @ApiModelProperty(value = "论文题目id")
    @NotNull(message = "题目信息不能为空")
    private Integer subjectId;

    @ApiModelProperty(value = "评语")
    private String comment;
}
