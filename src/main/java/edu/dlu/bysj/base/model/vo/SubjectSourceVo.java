package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/28 20:03
 */
@Data
@ApiModel(description = "题目来源返回类")
public class SubjectSourceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目来源id")
    private Integer subjectTypeId;
    @ApiModelProperty(value = "题目来源名称")
    private String subjectTypeName;
}
