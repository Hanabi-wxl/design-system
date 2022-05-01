package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * @author XiangXinGang
 * @date 2021/10/24 17:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "任务书审批表返回记录类")
public class TopicApprovalListQuery extends CommonPage {
  @Builder
  public TopicApprovalListQuery(
      @Min(value = 1, message = "pageSize is less than 1") Integer pageSize,
      @Min(value = 1, message = "pageNumber is less than 1 ") Integer pageNumber,
      Integer majorId,
      Integer type,
      String userNumber,
      String userName,
      String year) {
    super(pageSize, pageNumber);
    this.majorId = majorId;
    this.type = type;
    this.userNumber = userNumber;
    this.userName = userName;
    this.year = year;
  }

  @ApiModelProperty(value = "专业id")
  private Integer majorId;

  @ApiModelProperty(value = "类型")
  private Integer type;

  @ApiModelProperty(value = "学号/教工号")
  private String userNumber;

  @ApiModelProperty(value = "学生/老师姓名")
  private String userName;

  @ApiModelProperty(value = "年份")
  private String year;
}
