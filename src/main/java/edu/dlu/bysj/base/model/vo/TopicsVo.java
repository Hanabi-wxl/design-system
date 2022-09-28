package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 22:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "学生获取选题列表返回类")
public class TopicsVo {
    @ApiModelProperty(value = "题目id")
    private Integer subjectId;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "第一指导教师名称")
    private String firstTeacherName;
    @ApiModelProperty(value = "职称")
    private String firstTeacherTitle;
    @ApiModelProperty(value = "职称")
    private String firstTeacherPhone;
    @ApiModelProperty(value = "已选人数")
    private Integer selectNumber;
}
