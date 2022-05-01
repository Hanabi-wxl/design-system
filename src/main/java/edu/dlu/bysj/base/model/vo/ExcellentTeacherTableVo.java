package edu.dlu.bysj.base.model.vo;

import edu.dlu.bysj.base.model.vo.basic.BasicExcellentVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/8 20:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "返回优秀教师申报表部分内容类")
public class ExcellentTeacherTableVo {
    @ApiModelProperty(value = "总人数")
    private Integer total;

    List<BasicExcellentVo> info;

    @ApiModelProperty(value = "是否为优秀教师")
    private Integer isGood;
    @ApiModelProperty(value = "自我评语")
    private String comment;
}
