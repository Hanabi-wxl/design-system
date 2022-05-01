package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "答辩记录列表返回类")
public class RecordSubjectVo {
    @ApiModelProperty(value = "题目Id")
    private Integer subjectId;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "第一指导教师姓名")
    private String firstName;
    @ApiModelProperty(value = "题目名称")
    private String title;
    @ApiModelProperty(value = "学生名称")
    private String studentName;
    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    @ApiModelProperty(value = "题目进展状态")
    private Integer progress;

}
