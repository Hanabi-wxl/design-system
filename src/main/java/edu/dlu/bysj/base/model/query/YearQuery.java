package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author sinre
 * @create 06 20, 2022
 * @since 1.0.0
 */
@Data
public class YearQuery extends CommonPage {
    @NotNull(message = "当前年份不能为空")
    private Integer year;
}
