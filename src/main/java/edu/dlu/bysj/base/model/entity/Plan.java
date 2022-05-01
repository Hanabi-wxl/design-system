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
 * 周计划表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Plan对象", description="周计划表")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "周次")
    private Integer week;

    @ApiModelProperty(value = "计划内容")
    private String content;

    @ApiModelProperty(value = "此条信息是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
