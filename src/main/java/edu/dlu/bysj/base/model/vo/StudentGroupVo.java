package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/8 21:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "学生")
public class StudentGroupVo {
    @ApiModelProperty(value = "分组编号")
    private Integer groupNumber;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "答辩顺序")
    private Integer serial;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "学生名称")
    private String studentName;
    @ApiModelProperty(value = "要求")
    private String requirement;
}
