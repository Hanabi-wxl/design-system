package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/25 15:09
 */
@Data
@ApiModel(description = "学习简易信息返回类")
public class SchoolSimplifyVo implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "学校id")
  private Integer schoolId;

  @ApiModelProperty(value = "学校名称")
  private String schoolName;
}
