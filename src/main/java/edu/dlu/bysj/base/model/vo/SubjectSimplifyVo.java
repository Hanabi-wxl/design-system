package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/10/24 17:18
 */
@Data
@ApiModel(description = "题目简单信息返回类")
public class SubjectSimplifyVo {
  @ApiModelProperty(value = "题目id")
  private Integer subjectId;

  @ApiModelProperty(value = "题目名称")
  private String subjectName;

  @ApiModelProperty(value = "题目进展状态")
  private String progress;
}
