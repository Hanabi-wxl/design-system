package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/28 20:46
 */
@Data
@ApiModel(description = "题目审批表提交类")
public class SubjectApprovalVo {
    @ApiModelProperty(value = "题目名称")
    private String subjectName;

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;

    @ApiModelProperty(value = "学生账号")
    private Integer studentNumber;

    @ApiModelProperty(value = "学生账号")
    private Integer studentId;

    @ApiModelProperty(value = "是否是第一次指导 0 不是, 1 是")
    private Integer isFirst;

    @ApiModelProperty(value = "是否雷同 0雷同, 1不雷同")
    private Integer isSimilar;

    @ApiModelProperty(value = "第二指导老师id")
    private Integer secondTeacherId;

    @ApiModelProperty(value = "拟需学生人数")
    private Integer needStudentNumber;

    @ApiModelProperty(value = "论文类型id")
    private Integer paperTypeId;

    @ApiModelProperty(value = "题目来源id")
    private Integer sourceId;

    @ApiModelProperty(value = "内容摘要")
    private String abstractContext;

    @ApiModelProperty(value = "必要性")
    private String necessity;

    @ApiModelProperty(value = "可行性")
    private String feasibility;

    @ApiModelProperty(value = "将要分配给的专业")
    private List<Integer> majorIds;

    @ApiModelProperty(value = "题目id")
    private String subjectId;
}
