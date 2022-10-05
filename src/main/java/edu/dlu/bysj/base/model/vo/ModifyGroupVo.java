package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/8 21:28
 */

/*
 * @Description: fix
 * @Author: sinre
 * @Date: 2022/6/10 20:41
 * @param null
 * @return
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "新增/修改答辩分组信息参数接受类")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModifyGroupVo {
    @ApiModelProperty(value = "总id(如果为空则为新增，否则为修改)")
    private Integer id;

    @ApiModelProperty(value = "组号")
    private Integer teamNumber;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private String endTime;

    @ApiModelProperty(value = "地址")
    @NotBlank(message = "地址不能为空")
    private String address;

    @ApiModelProperty(value = "专业id")
    private Integer majorId;

    @ApiModelProperty(value = "要求")
    private String require;

    @ApiModelProperty(value = "分组类型")
    @NotNull(message = "分组类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "年级")
    private Integer grade;

    @ApiModelProperty(value = "是否二答")
    private Boolean isRepeat;
}
