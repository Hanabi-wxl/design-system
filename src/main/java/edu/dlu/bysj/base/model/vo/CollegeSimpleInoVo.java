package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangXinGang
 * @date 2021/10/30 21:02
 */
@Data
public class CollegeSimpleInoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "学院id")
    private Integer collegeId;

    @ApiModelProperty(value = "学院名称")
    private String collegeName;
}
