package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/8 15:56
 */
@Data
public class SimilarTeamStudentVo {
    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "subjectName")
    private String subjectName;

    @ApiModelProperty(value = "第一指导教师姓名")
    private String firstName;

    @ApiModelProperty(value = "第一指导教师职称")
    private String title;

    @ApiModelProperty(value = "学生名称")
    private String studentName;

    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;

    @ApiModelProperty(value = "学号")
    private String studentNumber;

    @ApiModelProperty(value = "题目进展状态")
    private String progress;
}
