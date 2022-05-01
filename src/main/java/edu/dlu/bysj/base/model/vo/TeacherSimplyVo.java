package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/28 17:06
 */
@Data
@ApiModel(description = "本专业老师简单信息返回类")
public class TeacherSimplyVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "教师id")
    private Integer teacherId;
    @ApiModelProperty(value = "教师姓名")
    private String teacherName;
}
