package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/10/8 15:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "过程管理详情类")
public class ProcessDetailVo {
    @ApiModelProperty(value = "周次")
    private Integer week;
    @ApiModelProperty(value = "学生填写内容")
    private String studentContent;
    @ApiModelProperty(value = "教师填写内容")
    private String teacherContent;
    @ApiModelProperty(value = "学生填写时间")
    private LocalDate studentTime;
    @ApiModelProperty(value = "指导教师姓名")
    private String teacherName;
    @ApiModelProperty(value = "教师填写时间")
    private LocalDate teacherTime;
}
