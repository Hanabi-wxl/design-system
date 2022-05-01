package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/2 15:48
 */
@Data
@ApiModel(description = "")
public class StudentInfoVo {
    @ApiModelProperty(value = "题目信息")
    private String subjectName;

    @ApiModelProperty(value = "题目编号")
    private Integer subjectId;

    @ApiModelProperty(value = "学生名称")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentNumber;

    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;

    @ApiModelProperty(value = "题目进展状态")
    private String progress;


}
