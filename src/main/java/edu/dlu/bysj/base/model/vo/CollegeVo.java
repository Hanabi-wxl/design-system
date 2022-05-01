package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/9 14:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "返回学院列表记录类")
public class CollegeVo {
    @ApiModelProperty(value = "学院id")
    private Integer collegeId;
    @ApiModelProperty(value = "学院名称")
    private String collegeName;
    @ApiModelProperty(value = "学院代码")
    private Integer collegeCode;
    @ApiModelProperty(value = "状态")
    private String status;
}
