package edu.dlu.bysj.base.model.vo;

import edu.dlu.bysj.base.model.vo.basic.BasicTotalConfigVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "新增/修改总评配置参数接受类")
public class ModifyTotalConfigVo extends BasicTotalConfigVo {
    @ApiModelProperty(value = "年级")
    private Integer grade;
    @ApiModelProperty(value = "专业")
    private Integer majorId;
    @ApiModelProperty(value = "配置id")
    private Integer configId;
}
