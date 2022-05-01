package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/30 11:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApproveConditionQuery extends CommonPage {
    @ApiModelProperty(value = "学号/教工号")
    private String userNumber;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "年份")
    @NotNull(message = "年份不能为空")
    private Integer year;

    @ApiModelProperty(value = "专业")
    @NotNull
    private Integer majorId;

    @ApiModelProperty(value = "用户类型（0：学生，1：教师）")
    @NotNull
    private Integer type;
}
