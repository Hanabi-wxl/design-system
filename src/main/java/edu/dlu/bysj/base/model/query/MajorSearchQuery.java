package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author sinre
 * @create 06 17, 2022
 * @since 1.0.0
 */
@Data
public class MajorSearchQuery extends CommonPage {
    @ApiModelProperty(value = "年级")
    @NotNull
    private Integer year;

    @ApiModelProperty(value = "专业id")
    @NotNull
    private Integer majorId;

    @ApiModelProperty(value = "教师名称/id")
    private String searchContent;
}
