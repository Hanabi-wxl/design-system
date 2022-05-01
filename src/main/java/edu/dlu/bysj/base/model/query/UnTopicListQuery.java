package edu.dlu.bysj.base.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/11/2 10:41
 */
@Data
@ApiModel(description = "未被选择中题目的查询列")
public class UnTopicListQuery {
    @ApiModelProperty(value = "教工号")
    private String teacherNumber;

    @ApiModelProperty(value = "教师名称")
    private String teacherName;

    @ApiModelProperty(value = "年级")
    @NotNull
    private Integer year;

    @ApiModelProperty(value = "专业id")
    @NotNull
    private Integer majorId;
}
