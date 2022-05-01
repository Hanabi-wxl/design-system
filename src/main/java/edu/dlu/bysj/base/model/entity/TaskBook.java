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
 * 任务书
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TaskBook对象", description="任务书")
public class TaskBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "选题依据")
    private String according;

    @ApiModelProperty(value = "论文要求")
    private String demand;

    @ApiModelProperty(value = "工作重点")
    private String emphasis;

    @ApiModelProperty(value = "文献目录")
    private String reference;

    @ApiModelProperty(value = "专业负责人是否同意(1:同意，0：不同意)")
    private Integer majorAgree;

    @ApiModelProperty(value = "专业负责人签署日期")
    private LocalDate majorDate;

    @ApiModelProperty(value = "专业负责人id")
    private Integer majorLeadingId;

    @ApiModelProperty(value = "院级负责人是否同意（1：同意，0：不同意）")
    private Integer collegeAgree;

    @ApiModelProperty(value = "院级负责人签署日期")
    private LocalDate collegeDate;

    @ApiModelProperty(value = "院级负责人id")
    private Integer collegeLeadingId;

    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "此条数据是否有效(1:有效,0无效)")
    @TableLogic
    private Integer status;


}
