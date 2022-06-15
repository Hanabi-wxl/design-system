package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/11/8 15:25
 */
@Data
@ApiModel(description = "新增/修改问题类")
public class DefenseRecordVo {
    @ApiModelProperty(value = "题目Id")
    @NotNull
    private Integer subjectId;

    @ApiModelProperty(value = "问题名称")
    @NotNull
    private Integer questionNumber;

    @ApiModelProperty(value = "问题名称")
    @NotBlank
    private String question;

    @ApiModelProperty(value = "问题回答")
    @NotBlank
    private String answer;

    @ApiModelProperty(value = "记录id（若新增，该字段为空）")
    private Integer id;

    @ApiModelProperty(value = "是否正确")
    @Range(min = 0, max = 1)
    private Integer correct;
}
