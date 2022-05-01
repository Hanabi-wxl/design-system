package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "获取答辩记录详情返回类")
public class RecordDetailVo {
    @ApiModelProperty(value = "问题名称")
    private String questionName;
    @ApiModelProperty(value = "时间")
    private String questionAnswer;
    @ApiModelProperty(value = "时间")
    private LocalDate  time;
    @ApiModelProperty(value = "是否正确")
    private Integer correct;
}
