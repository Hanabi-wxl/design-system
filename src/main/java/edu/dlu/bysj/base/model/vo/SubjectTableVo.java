package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/*
 * @Description:
 * @Author: sinre 
 * @Date: 2022/5/17 15:30
 * @param null
 * @return 
 **/
@Data
@ApiModel(description = "题目详情返回类")
public class SubjectTableVo {
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "论文类型")
    private String paperType;
    @ApiModelProperty(value = "题目来源")
    private String source;
    @ApiModelProperty(value = "第一指导教师名称")
    private String firstTeacherName;
    @ApiModelProperty(value = "第二指导教师名称")
    private String secondTeacherName;
    @ApiModelProperty(value = "第一指导教师职称")
    private String firstTeacherTitle;
    @ApiModelProperty(value = "第一指导教师专业")
    private String firstTeacherMajor;
    @ApiModelProperty(value = "第一指导教师教研室")
    private String firstTeacherOffice;
    @ApiModelProperty(value = "互评教师名称")
    private String otherTeacherName;
    @ApiModelProperty(value = "互评教师专业")
    private String otherTeacherMajor;
    @ApiModelProperty(value = "互评教师电话")
    private String otherTeacherPhone;
    @ApiModelProperty(value = "将要分配给的专业")
    private List<String> majors;
    @ApiModelProperty(value = "学生专业")
    private String studentMajor;
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    @ApiModelProperty(value = "班级名称")
    private String className;
    @ApiModelProperty(value = "是否为第一次指导")
    private String isFirst;
    @ApiModelProperty(value = "是否雷同")
    private String isSimilar;
    @ApiModelProperty(value = "拟需人数")
    private Integer needStudentNumber;
    @ApiModelProperty(value = "内容摘要")
    private String abstractContent;
    @ApiModelProperty(value = "必要性")
    private String necessity;
    @ApiModelProperty(value = "可行性")
    private String feasibility;
}
