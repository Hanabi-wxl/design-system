package edu.dlu.bysj.base.model.query.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

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
    @Min(value = 1, message = "每页记录数不能小于1")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页码")
    @Min(value = 1, message = "当前页码不能小于1")
    private Integer pageNumber;
}
