package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/28 20:46
 */
@Data
@ApiModel(description = "题目审批表提交类")
public class SubjectApprovalVo {
    @ApiModelProperty(value = "题目名称")
    @NotBlank(message = "课题名称不能为空")
    private String subjectName;

    @ApiModelProperty(value = "教师id")
    private Integer teacherId;

    @ApiModelProperty(value = "学生账号")
    private Integer studentNumber;

    @ApiModelProperty(value = "学生账号")
    private Integer studentId;

    @ApiModelProperty(value = "是否是第一次指导 0 不是, 1 是")
    @NotNull(message = "是否是第一次指导不能为空")
    private Integer isFirst;

    @ApiModelProperty(value = "是否雷同 0雷同, 1不雷同")
    @NotNull(message = "是否雷同不能为空")
    private Integer isSimilar;

    @ApiModelProperty(value = "第二指导老师id")
    private Integer secondTeacherId;

    @ApiModelProperty(value = "拟需学生人数")
    @NotNull(message = "拟需学生人数不能为空")
    private Integer needStudentNumber;

    @ApiModelProperty(value = "论文类型id")
    @NotNull(message = "论文类型不能为空")
    private Integer paperTypeId;

    @ApiModelProperty(value = "题目来源id")
    @NotNull(message = "题目来源不能为空")
    private Integer sourceId;

    @ApiModelProperty(value = "内容摘要")
    @NotNull(message = "内容摘要不能为空")
    private String abstractContext;

    @ApiModelProperty(value = "必要性")
    @NotBlank(message = "必要性不能为空")
    private String necessity;

    @ApiModelProperty(value = "可行性")
    @NotBlank(message = "可行性不能为空")
    private String feasibility;

    @ApiModelProperty(value = "将要分配给的专业")
    @NotEmpty(message = "将要分配给的专业不能为空")
    private List<Integer> majorIds;

    @ApiModelProperty(value = "题目id")
    private String subjectId;
}
