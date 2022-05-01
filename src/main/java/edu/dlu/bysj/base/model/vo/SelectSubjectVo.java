package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 22:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "专业管理员调配指导老师列表返回类")
public class SelectSubjectVo {
    @ApiModelProperty(value = "题目名称")
    private String  subjectName;
    @ApiModelProperty(value = "题目编号")
    private Integer subjectId;
    @ApiModelProperty(value = "学生名称")
    private String studentName;
    @ApiModelProperty(value = "学生学号")
    private String studentNumber;
    @ApiModelProperty(value = "学生电话号码")
    private String studentPhone;
    @ApiModelProperty(value = "题目进展状态")
    private Integer progress;
}
