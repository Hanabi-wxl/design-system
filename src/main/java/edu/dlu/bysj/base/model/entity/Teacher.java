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
 * 教工信息表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Teacher对象", description="教工信息表")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "教工号")
    private String teacherNumber;

    @ApiModelProperty(value = "专业编号")
    private Integer majorId;

    @ApiModelProperty(value = "科室id")
    private Integer officeId;

    @ApiModelProperty(value = "学位id")
    private Integer degreeId;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "电话号码")
    private String phoneNumber;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "职称id")
    private Integer titleId;

    @ApiModelProperty(value = "最近一次登录的IP")
    private String recentIp;

    @ApiModelProperty(value = "此条信息是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;

    @ApiModelProperty(value = "注册时间")
    private LocalDateTime registerDate;

    @ApiModelProperty(value = "是否可以登陆")
    private Integer canUse;


}
