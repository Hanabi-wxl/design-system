package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/2 10:38
 */
@Data
@ApiModel(description = "没有被选择题目的返回类列表")
public class UnSelectTopicVo {
    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "题目名称")
    private String subjectName;

    @ApiModelProperty(value = "第一指导教师姓名")
    private String firstTeacherName;

    @ApiModelProperty(value = "第一指导教师专业")
    private String firstTeacherMajor;
}
