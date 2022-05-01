package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 22:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "剩余论文题目列表")
public class RemainingPapersVo {
    @ApiModelProperty(value = "题目id")
    private Integer subjectId;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "第一指导教师姓名")
    private String firstTeacherName;
    @ApiModelProperty(value = "第一指导教师专业")
    private String firstTeacherMajor;
}
