package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/28 22:34
 */
@Data
@ApiModel(description = "教师简短信息返回类")
public class TeacherShortenVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "职称")
    private String title;
    @ApiModelProperty(value = "专业名称")
    private String major;
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    @ApiModelProperty(value = "学院名称")
    private String college;
}
