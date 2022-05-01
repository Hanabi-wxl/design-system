package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author XiangXinGang
 * @date 2021/10/9 11:27
 */
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "修改用户信息参数接受类")
public class ModifyUserVo {
    @ApiModelProperty(value = "用户账号")
    private String userNumber;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "性别1:男，0 :女")
    private String sex;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "职称id")
    private Integer titleId;
    @ApiModelProperty(value = "学位Id")
    private Integer degreeId;
    @ApiModelProperty(value = "专业id")
    private Integer majorId;
    @ApiModelProperty(value = "科室id")
    private Integer officeId;
    @ApiModelProperty(value = "是否为学生 1:是,0不是")
    private String isStudent;
    @ApiModelProperty(value = "班级id")
    private Integer classId;
    @ApiModelProperty(value = "用户id")
    private Integer userId;
}
