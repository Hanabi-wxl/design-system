package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author XiangXinGang
 * @date 2021/11/8 16:48
 */
@Data
public class RecordInfoVo {
    @ApiModelProperty(name = "问题名称")
    private String questionName;
    @ApiModelProperty(name = "问题答案")
    private String questionAnswer;
    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate time;
    @ApiModelProperty(value = "回答是否正确")
    private String correct;
}
