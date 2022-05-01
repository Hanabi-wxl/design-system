package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/25 15:48
 */
@Data
@ApiModel(description = "个人简易功能列表返回值类")
public class FunctionSimplifyVo implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "功能id")
  private Integer functionId;

  @ApiModelProperty(value = "功能时间")
  private String functionName;

  @ApiModelProperty(value = "父级id")
  private Integer fatherId;
}
