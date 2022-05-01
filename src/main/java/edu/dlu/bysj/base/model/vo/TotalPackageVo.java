package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/9 20:33
 */
@Data
@ApiModel(description = "对所有含有total属性的进行封装")
public class TotalPackageVo<T> {
    @ApiModelProperty(value = "总数")
    private  Integer total;
    @ApiModelProperty(value = "结果集合")
    private List<T> arrays;
}
