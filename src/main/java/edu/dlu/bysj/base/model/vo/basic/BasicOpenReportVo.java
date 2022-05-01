package edu.dlu.bysj.base.model.vo.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author XiangXinGang
 * @date 2021/10/8 15:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BasicOpenReportVo {
    @ApiModelProperty(value = "选题依据")
    private String according;
    @ApiModelProperty(value = "研究内容")
    private String content;
    @ApiModelProperty(value = "工作安排")
    private String schedule;
    @ApiModelProperty(value = "文献目录")
    private String reference;
    @ApiModelProperty(value = "文献综述")
    private String review;
}
