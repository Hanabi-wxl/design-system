package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/7 20:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubjectListQuery extends CommonPage {
    @Builder
    public SubjectListQuery(Integer pageSize, Integer pageNumber, Integer year) {
        super(pageSize, pageNumber);
        this.year = year;
    }

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;

    @ApiModelProperty(value = "当前年份")
    @NotNull(message = "年份不能为空")
    private Integer year;
}
