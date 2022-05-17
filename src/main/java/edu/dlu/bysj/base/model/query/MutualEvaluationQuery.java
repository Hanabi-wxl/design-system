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

    @ApiModelProperty(value = "姓名/工号")
    private String searchContent;

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;
}
