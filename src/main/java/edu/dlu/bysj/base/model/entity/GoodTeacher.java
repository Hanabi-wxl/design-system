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
 * 优秀教师表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GoodTeacher对象", description="优秀教师表")
public class GoodTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;

    @ApiModelProperty(value = "自我评语")
    private String selfComment;

    @ApiModelProperty(value = "院负责人是否同意（1：同意，0：不同意）")
    private Integer collegeAgree;

    @ApiModelProperty(value = "院级负责人id")
    private Integer collegeLeadingId;

    @ApiModelProperty(value = "院级负责人签署日期")
    private LocalDate collegeDate;

    @ApiModelProperty(value = "获得优秀称号的学年")
    private Integer schoolYear;

    @ApiModelProperty(value = "设置人id")
    private Integer setupPersonId;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
