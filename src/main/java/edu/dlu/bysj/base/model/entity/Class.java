package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 班级信息表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Class对象", description="班级信息表")
public class Class implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "班级名称（例如：软件192）")
    private String name;

    @ApiModelProperty(value = "年级（例如：2019）")
    private Integer grade;

    @ApiModelProperty(value = "专业id")
    private Integer majorId;

    @ApiModelProperty(value = "此条信息是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
