package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/8 21:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MutualEvaluationQuery extends CommonPage {
    @ApiModelProperty(value = "年份")
    @NotNull(message = "年级不能为空")
    private Integer year;

    @ApiModelProperty(value = "专业id")
    @NotNull(message = "专业id不能为空")
    private Integer majorId;

    @ApiModelProperty(value = "查询类型(1;老师,0学生)")
    private Integer type;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户编号")
    private String userNumber;

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;
}
