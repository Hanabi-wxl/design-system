package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/25 9:23
 */
@Data
@ApiModel(description = "科室简易信息vo")
public class OfficeSimplifyVo implements Serializable {
  @ApiModelProperty(value = "科室id")
  private Integer officeId;

  @ApiModelProperty(value = "科室名称")
  private String officeName;
}
