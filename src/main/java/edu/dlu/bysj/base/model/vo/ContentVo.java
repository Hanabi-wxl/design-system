package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/2 17:21
 */
@Data
@ApiModel(description = "每周计划内容")
public class ContentVo {
    @ApiModelProperty(name = "第几周")
    private Integer week;
    @ApiModelProperty(name = "该周的内容")
    private String content;
}
