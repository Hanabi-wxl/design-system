package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author XiangXinGang
 * @date 2021/10/9 11:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "获取用户列表返回值类")
public class UserVo {
    @ApiModelProperty(value = "用户编号（学号、教工号）")
    private String userNumber;
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "用户姓名")
    private String userName;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "班级")
    private Integer classId;
    @ApiModelProperty(value = "班级")
    private String className;
    @ApiModelProperty(value = "专业名称")
    private String majorName;
    @ApiModelProperty(value = "角色id")
    private Integer roleId;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    @ApiModelProperty(value = "账号状态")
    private Integer canUse;

}
