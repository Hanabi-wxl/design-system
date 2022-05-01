package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * @author XiangXinGang
 * @date 2021/11/2 17:18
 */
@Data
@ApiModel(description = "周计划提交对象")
public class PlanSubmitVo {
    @ApiModelProperty(name = "题目Id")
    @NotNull
    private Integer subjectId;

    @ApiModelProperty(name = "周计划内容")
    private ArrayList<ContentVo> content;
}
