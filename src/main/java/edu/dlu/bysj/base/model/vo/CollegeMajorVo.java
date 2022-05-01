package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/10/26 15:46
 */
@Data
@ApiModel(description = "教师所属学院的全部专业")
public class CollegeMajorVo {
  @ApiModelProperty(value = "专业id")
  private Integer majorId;

  @ApiModelProperty(value = "专业名称")
  private String majorName;
}
