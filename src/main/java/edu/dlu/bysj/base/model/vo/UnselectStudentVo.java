package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 22:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "获取未选题学生列表:返回值")
public class UnselectStudentVo {
    @ApiModelProperty(value = "学生id")
    private Integer studentId;
    @ApiModelProperty(value = "学生名称")
    private String studentName;
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    @ApiModelProperty(value = "班级名称")
    private String className;
}
