package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:00
 */
@Data
@ApiModel(value = "学位简易信息返回类")
public class DegreeSimplifyVo implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "学位id")
  private Integer degreeId;

  @ApiModelProperty(value = "学位名称")
  private String degreeName;
}
