package edu.dlu.bysj.base.model.vo.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author sinre
 * @create 05 05, 2022
 * @since 1.0.0
 */
@Data
public class CommonReviewsVo {
    @ApiModelProperty(value = "修改意见")
    private String comment;

    @ApiModelProperty(value = "题目id")
    @NotNull
    private String[] subjectIds;
}
