package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/8 16:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "分数获取评语返回类")
public class TeacherScoreTemplateVo {
    @ApiModelProperty(value = "选题质量")
    private Integer quality;
    @ApiModelProperty(value = "能力水平")
    private Integer ability;
    @ApiModelProperty(value = "完成质量")
    private Integer complete;
    @ApiModelProperty(value = "题目id")
    private Integer subjectId;
}
