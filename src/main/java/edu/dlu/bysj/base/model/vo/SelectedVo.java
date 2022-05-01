package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 22:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "获取学生已选题列表返回值")
public class SelectedVo {
    @ApiModelProperty(value = "题目id")
    private Integer subjectId;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "指导教师姓名")
    private String teacherName;
    @ApiModelProperty(value = "指导教师职称")
    private String title;
    @ApiModelProperty(value = "论文类型")
    private String paperType;
    @ApiModelProperty(value = "题目来源")
    private String subjectResource;
    @ApiModelProperty(value = "修改次数")
    private Integer changeNumber;
}
