package edu.dlu.bysj.base.model.vo.basic;

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
public class CommonReviewVo {
    @ApiModelProperty(value = "是否同意(1：同意，0：不同意）")
    @NotNull
    private Integer agree;

    @ApiModelProperty(value = "审阅类型审阅类型(1:校级,0:专业级)")
    private Integer type;

    @ApiModelProperty(value = "修改意见")
    private String comment;

    @ApiModelProperty(value = "题目id")
    @NotNull
    private String subjectId;
}
