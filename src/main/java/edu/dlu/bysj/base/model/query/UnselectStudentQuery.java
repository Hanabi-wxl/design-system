package edu.dlu.bysj.base.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/11/1 16:24
 */
@Data
@ApiModel(description = "获取当前未选题学生")
public class UnselectStudentQuery {
    @ApiModelProperty(value = "学号")
    private String studentNumber;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "年份")
    @NotNull(message = "年份不能为空")
    private Integer year;

    @ApiModelProperty(value = "专业id")
    private Integer majorId;
}
