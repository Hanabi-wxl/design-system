package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author XiangXinGang
 * @date 2021/10/9 15:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "新增/修改班级参数接受值")
public class ModifyClass {
  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "年级")
  private Integer grade;

  @ApiModelProperty(value = "专业id")
  private Integer majorId;

  @ApiModelProperty(value = "班级Id")
  private Integer classId;
}
