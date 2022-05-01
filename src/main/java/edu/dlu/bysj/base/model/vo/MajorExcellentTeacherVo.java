package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/8 20:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "优秀教师审阅列表返回类")
public class MajorExcellentTeacherVo {
    @ApiModelProperty(value = "教师姓名")
    private String teacherName;
    @ApiModelProperty(value = "教工号")
    private String teacherNumber;
    @ApiModelProperty(value = "老师手机号")
    private String teacherPhone;
    @ApiModelProperty(value = "A段人数")
    private Integer ANumber;
    @ApiModelProperty(value = "B段人数")
    private Integer BNumber;
    @ApiModelProperty(value = "C段人数")
    private Integer CNumber;
    @ApiModelProperty(value = "D段人数")
    private Integer DNumber;
    @ApiModelProperty(value = "F段人数")
    private Integer FNumber;
    @ApiModelProperty(value = "二答人数")
    private Integer secondNumber;
    @ApiModelProperty(value = "是否优秀教师（1：是，0：不是）")
    private Integer isGood;
    @ApiModelProperty(value = "教师id")
    private Integer teacherId;
}
