package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/8 20:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "查看教师本人评优情况返回类")
public class TeacherYearEvaluationVo {
    @ApiModelProperty(value = "年份")
    private Integer year;
    @ApiModelProperty(value = "成绩A(人)")
    private Integer A;
    @ApiModelProperty(value = "成绩B(人)")
    private Integer B;
    @ApiModelProperty(value = "成绩C(人)")
    private Integer C;
    @ApiModelProperty(value = "成绩D(人)")
    private Integer D;
    @ApiModelProperty(value = "成绩F(人)")
    private Integer F;
    @ApiModelProperty(value = "二答人数")
    private Integer isSecond;

}
