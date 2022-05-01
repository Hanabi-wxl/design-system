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
 * 中期检查表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MiddleCheck对象", description="中期检查表")
public class MiddleCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "难易程度,0表示偏难,1表示适中,2表示偏易")
    private Integer difficulty;

    @ApiModelProperty(value = "文献数量")
    private Integer literatureQuantity;

    @ApiModelProperty(value = "工作量,0表示较少,1表示适中,2表示较多")
    private Integer workload;

    @ApiModelProperty(value = "初稿完成时间")
    private LocalDate finishDate;

    @ApiModelProperty(value = "任务书,0表示无,1表示有")
    private Integer hasTaskbook;

    @ApiModelProperty(value = "开题报告,0表示无,1表示有")
    private Integer hasOpenreport;

    @ApiModelProperty(value = "学习态度,0表示好,1表示一般,2表示差")
    private Integer attitude;

    @ApiModelProperty(value = "工作进度,0表示快,1表示按计划进行,2表示慢")
    private Integer workingSpeed;

    @ApiModelProperty(value = "中期工作结论,0表示优,1表示良,2表示中,3表示差")
    private Integer conclude;

    @ApiModelProperty(value = "调整情况")
    private String arrange;

    @ApiModelProperty(value = "调整时间")
    private LocalDate arrangeDate;

    @ApiModelProperty(value = "专业负责人意见")
    private Integer majorAgree;

    @ApiModelProperty(value = "专业负责人id")
    private Integer majorLeadingId;

    @ApiModelProperty(value = "专业负责人签署日期")
    private LocalDate majorDate;

    @ApiModelProperty(value = "院级负责人意见")
    private Integer collegeAgree;

    @ApiModelProperty(value = "院级负责人id")
    private Integer collegeLeadingId;

    @ApiModelProperty(value = "院级负责人签署日期")
    private LocalDate collegeDate;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
