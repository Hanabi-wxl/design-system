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
 * @date 2021/10/8 21:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "获取教师答辩信息返回类")
public class ReplyTeacherVo {
    @ApiModelProperty(value = "分组编号")
    private Integer groupNumber;
    @ApiModelProperty(value = "专业名称")
    private String major;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    
    @ApiModelProperty("答辩地址")
    private String address;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "教师姓名")
    private String teacherName;
    @ApiModelProperty(value = "是否二答")
    private Integer isSecond;

}
