package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:47
 */
@Data
@ApiModel(description = "简易的班级信息封装类")
public class ClassSimplifyVo implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "班级id")
  private Integer classId;

  @ApiModelProperty(value = "班级名称")
  private String className;
}
