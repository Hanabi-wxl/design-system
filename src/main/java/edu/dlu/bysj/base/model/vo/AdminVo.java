package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/9 11:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "返回管理员名单对应类")
public class AdminVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "教工号")
    private String teacherNumber;

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    @ApiModelProperty(value = "专业名称")
    private String majorName;

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "教师电话号码")
    private String teacherPhone;

    @ApiModelProperty(value = "教师邮箱")
    private String teacherEmail;
}
