package edu.dlu.bysj.base.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/9 11:13
 */
@ApiModel(description = "时间功能表记录类")
public class FunctionTimeVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public FunctionTimeVo() {
    }

    @ApiModelProperty(value = "功能名称")
    private String functionName;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "父级id")
    private Integer fatherId;
    @ApiModelProperty(value = "功能id")
    private Integer functionId;


    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

}
