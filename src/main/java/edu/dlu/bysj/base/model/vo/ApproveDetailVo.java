package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 21:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "题目审批列表内容返回类")
public class ApproveDetailVo {
    @ApiModelProperty(value = "题目编号")
    private Integer id;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "题目id")
    private String subjectId;
    @ApiModelProperty(value = "第一指导教师姓名")
    private String firstTeacherName;
    @ApiModelProperty(value = "第一指导教师手机号")
    private String firstTeacherPhone;
    @ApiModelProperty(value = "第一指导老师职称")
    private String firstTeacherTitle;
    @ApiModelProperty(value = "第二指导老师姓名")
    private String secondTeacherName;
    @ApiModelProperty(value = "第二指导老师手机号")
    private String secondTeacherPhone;
    @ApiModelProperty(value = "第二指导老师职称")
    private String secondTeacherTitle;
    @ApiModelProperty(value = "学生名称")
    private String studentName;
    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    @ApiModelProperty(value = "题目进展状态")
    private String progress;
    @ApiModelProperty(value = "归档编号")
    private Integer fillingNumber;
}
