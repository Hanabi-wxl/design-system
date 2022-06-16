package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 21:07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "教师自带题目列表")
public class SubjectDetailVo {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "题目id")
    private String subjectId;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;
    @ApiModelProperty(value = "题目进展状态")
    private String progress;
    @ApiModelProperty(value = "专业名称")
    private String major;
    @ApiModelProperty(value = "班级名称")
    private String className;
    @ApiModelProperty(value = "学生id")
    private Integer studentId;
    @ApiModelProperty(value = "学院名称")
    private String college;
    @ApiModelProperty(value = "年级")
    private String grade;
    @ApiModelProperty(value = "教师姓名")
    private String firstTeacherName;
    @ApiModelProperty(value = "教师手机号")
    private String firstTeacherPhone;
    @ApiModelProperty(value = "总分")
    private Integer summaryScore;
    @ApiModelProperty(value = "指导教师评价分")
    private Integer teacherScore;
    @ApiModelProperty(value = "过程评价分")
    private Integer processScore;
    @ApiModelProperty(value = "互评评价分")
    private Integer otherScore;
    @ApiModelProperty(value = "答辩评价分")
    private Integer defenceScore;
}
