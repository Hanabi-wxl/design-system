package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/8 16:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "按分组获取学生成绩返回类")
public class GroupScoreVo {
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    @ApiModelProperty(value = "学生手机号")
    private String studentPhone;
    @ApiModelProperty(value = "答辩序列号")
    private String serial;
    @ApiModelProperty(value = "自评成绩")
    private Integer teacherScore;
    @ApiModelProperty(value = "过程成绩")
    private Integer processScore;
    @ApiModelProperty(value = "互评成绩")
    private Integer otherScore;
    @ApiModelProperty(value = "总评成绩-自述总结")
    private Integer summary;
    @ApiModelProperty(value = "总评成绩-答辩过程")
    private Integer process;
    @ApiModelProperty(value = "总评成绩-选题质量")
    private Integer quality;
    @ApiModelProperty(value ="总评成绩-完成质量" )
    private Integer completeQuality;
    @ApiModelProperty(value = "总评成绩-能力水平")
    private Integer ability;
    @ApiModelProperty(value = "是否二答")
    private Integer isSecond;
    @ApiModelProperty(value = "题目id")
    private Integer subjectId;
}
