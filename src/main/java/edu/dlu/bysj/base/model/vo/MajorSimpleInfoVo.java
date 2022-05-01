package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/30 20:35
 */
@Data
public class MajorSimpleInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专业Id")
    @NotNull
    private Integer majorId;

    @ApiModelProperty(value = "专业名称")
    @NotNull
    private String majorName;
}
