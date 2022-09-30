package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/8 22:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "学生申请答辩返回结果类")
public class ApplyReplyVo {
    @ApiModelProperty(value = "需要的材料（材料齐全则为空）")
    private List<String> need;
    @ApiModelProperty(value = "分组编号")
    private String groupNumber;
    @ApiModelProperty(value = "答辩序号")
    private Integer serial;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "角色编号")
    private String roleName;

    @ApiModelProperty(value = "要求")
    private String requirement;
}
