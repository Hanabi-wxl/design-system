package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 20:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "返回教师基本信息类")
public class BaseTeacherVo {
    @ApiModelProperty(value = "教师职称名称")
    private String title;
    @ApiModelProperty(value = "专业名称")
    private String  major;
    @ApiModelProperty(value = "电子邮箱")
    private String  email;
    @ApiModelProperty(value = "学院名称")
    private String college;
}
