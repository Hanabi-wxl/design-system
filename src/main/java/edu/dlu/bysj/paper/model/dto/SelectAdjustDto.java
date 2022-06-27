package edu.dlu.bysj.paper.model.dto;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author sinre
 * @create 06 26, 2022
 * @since 1.0.0
 */
@Data
public class SelectAdjustDto extends CommonPage {
    @NotNull(message = "年份不能为空")
    private Integer year;
    @NotNull(message = "专业信息不能为空")
    private Integer majorId;

    private String searchContent;
}
