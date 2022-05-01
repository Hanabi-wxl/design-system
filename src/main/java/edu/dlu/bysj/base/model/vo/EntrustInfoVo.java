package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/10/31 9:15
 */
@Data
@ApiModel(value = "题目委托列表返会类")
public class EntrustInfoVo {
    @ApiModelProperty(value = "题目编号")
    private Integer subjectId;

    @ApiModelProperty(value = "题目名称")
    private String subjectNames;

    @ApiModelProperty(value = "第一指导教师姓名")
    private String firstName;

    @ApiModelProperty(value = "第一指导教师职称")
    private String firstTitle;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentNumber;

    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;

    @ApiModelProperty(value = "题目进展状态")
    private String progress;

    @ApiModelProperty(value = "归档编号")
    private String fillingNumber;
}
