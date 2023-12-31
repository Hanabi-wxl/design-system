package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/7 21:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubjectApproveListQuery extends CommonPage {
    @Builder
    public SubjectApproveListQuery(Integer pageSize, Integer pageNumber, Integer teacherId, Integer year) {
        super(pageSize, pageNumber);
        this.teacherId = teacherId;
        this.year = year;
    }

    @ApiModelProperty(value = "专业id")
    @NotNull(message = "专业id不能为空")
    private Integer majorId;

    @ApiModelProperty(value = "学院id")
    @NotNull(message = "学院id不能为空")
    private Integer collegeId;

    @ApiModelProperty(value = "当前年份")
    @NotNull(message = "当前年份不能为空")
    private Integer year;

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;

    @ApiModelProperty(value = "搜索内容 姓名或工号")
    private String searchContent;

}
