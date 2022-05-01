package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/10/31 17:00
 */
@Data
@ApiModel(description = "教师以确定学生题目列表记录类")
public class DetermineStudentVo {

    @ApiModelProperty(value = "题目名称")
    private Integer subjectId;

    @ApiModelProperty(value = "学生名称")
    private String studentName;

    @ApiModelProperty(value = "题目名称")
    private String studentNumber;

    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;

    @ApiModelProperty(value = "学生专业")
    private String studentMajor;

    @ApiModelProperty(value = "学生专业")
    private String studentClass;

    @ApiModelProperty(value = "题目名称")
    private String subjectName;

    @ApiModelProperty(value = "题目进展状况")
    private String progress;
}
