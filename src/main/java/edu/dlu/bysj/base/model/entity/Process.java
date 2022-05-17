package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 过程管理表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Process对象", description="过程管理表")
public class Process implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "周次")
    private Integer week;

    @ApiModelProperty(value = "教师评语")
    private String teacherContent;

    @ApiModelProperty(value = "教师评语日期")
    private LocalDate teacherDate;

    @ApiModelProperty(value = "学生过程内容")
    private String studentContent;

    @ApiModelProperty(value = "学生评价日期")
    private LocalDate studentDate;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
