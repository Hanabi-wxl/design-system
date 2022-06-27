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
 * 论文题目表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Subject对象", description = "论文题目表")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目编号")
    private String subjectId;

    @ApiModelProperty(value = "题目名")
    private String subjectName;

    @ApiModelProperty(value = "题目类型id")
    private Integer subjectTypeId;

    @ApiModelProperty(value = "专业类型id")
    private Integer majorId;

    @ApiModelProperty(value = "第一指导老师id")
    private Integer firstTeacherId;

    @ApiModelProperty(value = "第二指导老师id")
    private Integer secondTeacherId;

    @ApiModelProperty(value = "学生id")
    private Integer studentId;

    @ApiModelProperty(value = "是否第一次指导")
    private Integer isFirstTeach;

    @ApiModelProperty(value = "三年内题目是否雷同")
    private Integer isSimilar;

    @ApiModelProperty(value = "拟需学生人数")
    private Integer studentNeeded;

    @ApiModelProperty(value = "年级")
    private Integer grade;

    @ApiModelProperty(value = "题目来源id")
    private Integer sourceId;

    @ApiModelProperty(value = "题目摘要")
    private String titleAbstract;

    @ApiModelProperty(value = "可行性")
    private String feasibility;

    @ApiModelProperty(value = "必要性")
    private String necessity;

    @ApiModelProperty(value = "题目提交日期")
    private LocalDate submitDate;

    @ApiModelProperty(value = "专业负责人是否同意（1：同意，0：不同意）")
    private Integer majorAgree;

    @ApiModelProperty(value = "专业负责人id")
    private Integer majorLeadingId;

    @ApiModelProperty(value = "专业负责人审核日期")
    private LocalDate majorDate;

    @ApiModelProperty(value = "院级负责人是否同意")
    private Integer collegeAgree;

    @ApiModelProperty(value = "院级负责人id")
    private Integer collegeLeadingId;

    @ApiModelProperty(value = "院级负责人审核日期")
    private LocalDate collegeDate;

    @ApiModelProperty(value = "题目进展情况id")
    private Integer progressId;

    @ApiModelProperty(value = "归档编号")
    private Integer filingNumber;

    @ApiModelProperty(value = "此条信息是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
