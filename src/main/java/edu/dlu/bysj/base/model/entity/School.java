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
 * 学校信息表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="School对象", description="学校信息表")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "学校名称")
    private String name;

    @ApiModelProperty(value = "学校代码")
    private Integer code;

    @ApiModelProperty(value = "学校地址")
    private String adress;

    @ApiModelProperty(value = "此条信息是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
