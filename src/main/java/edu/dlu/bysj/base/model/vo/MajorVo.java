package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/9 15:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "专业列表返回类")
public class MajorVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "专业id")
  private Integer majorId;

  @ApiModelProperty(value = "专业代码")
  private Integer majorCode;

  @ApiModelProperty(value = "专业名称")
  private String majorName;

  @ApiModelProperty(value = "状态")
  private Integer status;
}
