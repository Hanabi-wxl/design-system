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
 * 选题表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Topics对象", description="选题表")
public class Topics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "学生id")
    private Integer studentId;

    @ApiModelProperty(value = "第一志愿")
    private String firstSubjectId;

    @ApiModelProperty(value = "第二志愿")
    private String secondSubjectId;

    @ApiModelProperty(value = "此条记录是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;

    @ApiModelProperty(value = "第一志愿操作次数")
    private Integer firstChange;

    @ApiModelProperty(value = "第二志愿操作次数")
    private Integer secondChange;



}
