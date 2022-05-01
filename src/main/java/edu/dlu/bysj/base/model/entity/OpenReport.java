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
import java.time.LocalDateTime;

/**
 * <p>
 * 开题报告表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OpenReport对象", description="开题报告表")
public class OpenReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "教师评语")
    private String teacherComment;

    @ApiModelProperty(value = "教师评语日期")
    private LocalDate teacherDate;

    @ApiModelProperty(value = "专业负责人意见")
    private String majorAgree;

    @ApiModelProperty(value = "专业负责人id")
    private Integer majorLeadingId;

    @ApiModelProperty(value = "专业负责人签署日期")
    private LocalDate majorDate;

    @ApiModelProperty(value = "学院意见")
    private String collegeAgree;

    @ApiModelProperty(value = "学院签署人id")
    private Integer collegeLeadingId;

    @ApiModelProperty(value = "学院签署日期")
    private LocalDate collegeDate;

    @ApiModelProperty(value = "未合格原因")
    private String failReason;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime commitDate;

    @ApiModelProperty(value = "选题依据")
    private String according;

    @ApiModelProperty(value = "研究内容")
    private String searchContent;

    @ApiModelProperty(value = "工作安排")
    private String schedule;

    @ApiModelProperty(value = "参考文献")
    private String reference;

    @ApiModelProperty(value = "文献综述")
    private String literature;

    @ApiModelProperty(value = "文件id")
    private Integer fileId;

    @ApiModelProperty(value = "此条记录是否有效:(1:有效, 0：无效)")
    @TableLogic
    private Integer status;


}
