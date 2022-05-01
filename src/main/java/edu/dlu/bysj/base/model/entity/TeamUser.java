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
 * 答辩组和用户关系表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TeamUser对象", description="答辩组和用户关系表")
public class TeamUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "是否是学生(1：是学生,0 不是学生)")
    private Integer isStudent;

    @ApiModelProperty(value = "答辩分组id")
    private Integer teamId;

    @ApiModelProperty(value = "职责,  0表示组长,1表示副组长,2表示秘书,3表示组员,4表示答辩人")
    private Integer resposiblity;

    @ApiModelProperty(value = "该组的第几个答辩人")
    private Integer serial;

    @ApiModelProperty(value = "词条数据是否有效（0：无效，1：有效）")
    @TableLogic
    private Integer status;


}
