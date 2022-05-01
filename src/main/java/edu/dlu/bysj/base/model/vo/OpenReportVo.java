package edu.dlu.bysj.base.model.vo;

import edu.dlu.bysj.base.model.vo.basic.BasicOpenReportVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author XiangXinGang
 * @date 2021/10/8 15:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "修改开题报告参数接受类")
public class OpenReportVo extends BasicOpenReportVo {
    @Builder
    public OpenReportVo(String according, String content, String schedule, String reference, String review, Integer subjectId) {
        super(according, content, schedule, reference, review);
        this.subjectId = subjectId;
    }

    @ApiModelProperty(value = "题目信息")
    @NotNull
    private Integer subjectId;

}
