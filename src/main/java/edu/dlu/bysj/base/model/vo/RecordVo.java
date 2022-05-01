package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "新增/修改问题参数接收类")
public class RecordVo {
    @ApiModelProperty(value = "题目Id")
    private Integer subjectId;
    @ApiModelProperty(value = "问题名称")
    private String questionName;
    @ApiModelProperty(value = "问题回答")
    private String questionAnswer;
    @ApiModelProperty(value = "记录id(若为新增问题则该记录为空)")
    private Integer recordId;
    @ApiModelProperty(value = "回答是否正确")
    private Integer correct;
}
