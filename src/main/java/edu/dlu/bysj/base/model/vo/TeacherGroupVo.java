package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/8 21:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "提交教师分组数据参数接收类")
public class TeacherGroupVo {
    @ApiModelProperty(value = "分组信息id")
    @NotNull
    private Integer groupId;

    @ApiModelProperty(value = "教师id")
    @NotNull
    private Integer teacherId;

    @ApiModelProperty(value = "职责,  0表示组长,1表示副组长,2表示秘书,3表示组员,4表示答辩人")
    @NotNull
    private Integer resposiblity;

    @ApiModelProperty(value = "是否二答")
    private Integer isSecond;
}
