package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/7 22:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class OpenReportReviewsVo {
    @ApiModelProperty(value = "修改意见")
    private String comment;

    @ApiModelProperty(value = "题目id")
    @NotNull
    private String[] subjectIds;
}
