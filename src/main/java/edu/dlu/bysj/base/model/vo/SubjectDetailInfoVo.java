package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/29 15:30
 */
@Data
@ApiModel(description = "题目详情返回类")
public class SubjectDetailInfoVo {
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "教师id")
    private Integer firstTeacherId;
    @ApiModelProperty(value = "第二指导老师id")
    private Integer secondTeacherId;
    @ApiModelProperty(value = "论文类型id")
    private Integer paperTypeId;
    @ApiModelProperty(value = "题目来源id")
    private Integer sourceId;
    @ApiModelProperty(value = "第一指导教师名称")
    private String teacherName;
    @ApiModelProperty(value = "第一指导教师职称")
    private String title;
    @ApiModelProperty(value = "第一指导教师专业")
    private String major;
    @ApiModelProperty(value = "将要分配给的专业")
    private List<Integer> majorIds;
    @ApiModelProperty(value = "学生专业")
    private String studentMajor;
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    @ApiModelProperty(value = "班级名称")
    private String className;
    @ApiModelProperty(value = "是否为第一次指导")
    private Integer isFirst;
    @ApiModelProperty(value = "是否雷同")
    private Integer isSimilar;
    @ApiModelProperty(value = "第二指导教师姓名")
    private String secondTeacherName;
    @ApiModelProperty(value = "拟需人数")
    private Integer needStudentNumber;
    @ApiModelProperty(value = "论文类型")
    private String paperType;
    @ApiModelProperty(value = "题目来源")
    private String subjectResource;
    @ApiModelProperty(value = "内容摘要")
    private String abstractContent;
    @ApiModelProperty(value = "必要性")
    private String necessity;
    @ApiModelProperty(value = "可行性")
    private String feasibility;
}
