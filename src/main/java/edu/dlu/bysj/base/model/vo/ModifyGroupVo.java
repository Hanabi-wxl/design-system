package edu.dlu.bysj.base.model.vo;

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
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "新增/修改答辩分组信息参数接受类")
public class ModifyGroupVo {
    @ApiModelProperty(value = "分组id(如果为空则为新增，否则为修改)")
    private Integer groupId;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @Future(message = "结束时间必须是一个未来时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "地址")
    @NotBlank(message = "地址不能为空")
    private String address;

    @ApiModelProperty(value = "要求")
    private String requirement;

    @ApiModelProperty(value = "分组编号")
    private Integer groupNumber;

    @ApiModelProperty(value = "年级")
    @NotNull
    private Integer grade;

    @ApiModelProperty(value = "是否二答")
    @NotNull
    private Integer isSecond;
}
