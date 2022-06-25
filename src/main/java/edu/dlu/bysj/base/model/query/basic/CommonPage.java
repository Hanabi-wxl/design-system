package edu.dlu.bysj.base.model.query.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/7 21:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CommonPage {
    @ApiModelProperty(value = "每页记录数")
    @NotNull(message = "页面大小不能为空")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页码")
    @NotNull(message = "页码不能为空")
    private Integer pageNumber;
}
