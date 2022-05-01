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
 *  总评模板表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TotalTemplate对象", description=" 总评模板表")
public class TotalTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "专业id")
    private Integer majorId;

    @ApiModelProperty(value = "评价内容")
    private String content;

    @ApiModelProperty(value = "总评要求")
    private String requestment;

    @ApiModelProperty(value = "总分")
    private Integer totalScore;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0 :无效）")
    @TableLogic
    private Integer status;


}
