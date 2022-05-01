package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/25 14:51
 */
@Data
@ApiModel(description = "简易的角色信息")
public class RoleSimplifyVo implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "角色Id")
  private Integer roleId;

  @ApiModelProperty(value = "角色名称")
  private String roleName;
}
