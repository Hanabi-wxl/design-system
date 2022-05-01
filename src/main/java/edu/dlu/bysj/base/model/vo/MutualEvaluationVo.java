package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/8 21:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "互评分组列表返回类")
public class MutualEvaluationVo {
    @ApiModelProperty(value = "题目id")
    private Integer subjectId;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "教师姓名")
    private String teacherName;
    @ApiModelProperty(value = "教师职称")
    private String title;
    @ApiModelProperty(value = "学生名称")
    private String studentName;
    @ApiModelProperty(value = "班级名称")
    private String className;
    @ApiModelProperty(value = "互评教师名称")
    private String otherTeacher;
    @ApiModelProperty(value = "题目进展状态")
    private String progress;
    @ApiModelProperty(value = "老师手机号")
    private String teacherPhone;
    @ApiModelProperty(value = "教师专业名称")
    private String teacherMajor;
}
