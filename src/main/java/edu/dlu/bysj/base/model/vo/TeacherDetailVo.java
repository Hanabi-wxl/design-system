package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/8 15:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "获取本专业教师信息记录返回类")
public class TeacherDetailVo implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "姓名")
  private String username;

  @ApiModelProperty(value = "性别")
  private String sex;

  @ApiModelProperty(value = "教工号")
  private String teacherNumber;

  @ApiModelProperty(value = "id号")
  private Integer userId;

  @ApiModelProperty(value = "学院名称")
  private String college;

  @ApiModelProperty(value = "专业名称")
  private String majorName;

  @ApiModelProperty(value = "专业id")
  private Integer majorId;

  @ApiModelProperty(value = "学院id")
  private Integer collegeId;

  @ApiModelProperty(value = "科室名称")
  private String office;

  @ApiModelProperty(value = "学位名称")
  private String degree;

  @ApiModelProperty(value = "手机号")
  private String phone;

  @ApiModelProperty(value = "电子邮箱")
  private String email;

  @ApiModelProperty(value = "职称名称")
  private String title;

  @ApiModelProperty(value = "教师id")
  private String teacherId;

  @ApiModelProperty(value = "有效")
  private Integer canUse;
}
