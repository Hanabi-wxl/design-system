package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/10/8 14:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "教师提交任务书返回类")
public class TaskBookVo {
    @ApiModelProperty(value = "题目id")
    @NotNull
    private Integer subjectId;

    @ApiModelProperty(value = "开始时间")
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty(value = "结束时间")
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty(value = "选题依据")
    private String according;

    @ApiModelProperty(value = "论文要求")
    private String demand;

    @ApiModelProperty(value = "工作重点")
    private String emphasis;

    @ApiModelProperty(value = "参考文献")
    private String reference;
}
