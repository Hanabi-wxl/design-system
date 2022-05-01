package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/8 15:51
 */
@Data
@ApiModel(description = "专业id")
public class DefenseRecordQuery extends CommonPage {
    @ApiModelProperty(value = "专业id")
    private Integer majorId;

    @ApiModelProperty(value = "分组编号")
    private Integer groupNumber;

    @ApiModelProperty(value = "年级")
    private Integer grade;


}
