package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author XiangXinGang
 * @date 2021/10/8 15:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@Builder
@ApiModel(description = "获取学生信息返回类")
public class StudentDetailVo {
    @ApiModelProperty(value = "姓名")
    private String username;
    @ApiModelProperty(value = "id")
    private Integer userId;
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    @ApiModelProperty(value = "专业")
    private String major;
    @ApiModelProperty(value = "专业id")
    private Integer majorId;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "班级名称")
    private String className;
    @ApiModelProperty(value = "班级id")
    private Integer classId;
    @ApiModelProperty(value = "学生id")
    private Integer studentId;
    @ApiModelProperty(value = "学院id")
    private Integer collegeId;
    @ApiModelProperty(value = "可使用")
    private Integer canUse;
    public StudentDetailVo() {
    }
}
