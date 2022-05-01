package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/24 17:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "任务书审批表返回类")
public class ApprovalTaskBookVo {
  @ApiModelProperty(value = "题目id")
  private Integer subjectId;

  @ApiModelProperty(value = "题目名称")
  private String subjectName;

  @ApiModelProperty(value = "第一指导老师姓名")
  private String firstName;

  @ApiModelProperty(value = "第一指导老师职称")
  private String firstTitle;

  @ApiModelProperty(value = "学号")
  private String studentNumber;

  @ApiModelProperty(value = "学生姓名")
  private String studentName;

  @ApiModelProperty(value = "学生手机号")
  private String studentPhone;

  @ApiModelProperty(value = "工作进展状态")
  private String progress;
}
