package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 学生信息表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Student对象", description="学生信息表")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "学号")
    private String studentNumber;

    @ApiModelProperty(value = "专业号")
    private Integer majorId;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "电话号码")
    private String phoneNumber;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "班级id")
    private Integer classId;

    @ApiModelProperty(value = "最近一次登录的IP")
    private String recentIp;

    @ApiModelProperty(value = "已确定的题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "注册时间")
    private LocalDateTime registerDate;

    @ApiModelProperty(value = "此条信息是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;

    @ApiModelProperty(value = "是否可以登陆(1:可以登录, 0 无法登录)")
    private Integer canUse;


}
