package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 成绩表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Score对象", description="成绩表")
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "过程评价-工作态度")
    private Integer processAttitude;

    @ApiModelProperty(value = "过程评价-遵守记录")
    private Integer processDiscipline;

    @ApiModelProperty(value = "过程评价-开题报告")
    private Integer processReport;

    @ApiModelProperty(value = "过程评价-任务完成")
    private Integer processComplete;

    @ApiModelProperty(value = "过程评价-评语")
    private String processComment;

    @ApiModelProperty(value = "过程评价-查重成绩")
    private Integer processSimilar;

    @ApiModelProperty(value = "过程评价-评价日期")
    private LocalDate processDate;

    @ApiModelProperty(value = "第一指导老师-选题质量")
    private Integer firstQuality;

    @ApiModelProperty(value = "第一指导老师-能力水平")
    private Integer firstAbility;

    @ApiModelProperty(value = "第一指导老师-完成质量")
    private Integer firstComplete;

    @ApiModelProperty(value = "指导教师-评语")
    private String firstComment;

    @ApiModelProperty(value = "指导老师-评价日期")
    private LocalDate firstDate;

    @ApiModelProperty(value = "互评老师-选题质量")
    private Integer otherQuality;

    @ApiModelProperty(value = "互评老师-能力水平")
    private Integer otherAbility;

    @ApiModelProperty(value = "互评老师-完成质量")
    private Integer otherComplete;

    @ApiModelProperty(value = "互评老师-评语")
    private String otherComment;

    @ApiModelProperty(value = "互评评价-签署人id")
    private Integer otherPersonId;

    @ApiModelProperty(value = "互评老师-评价日期")
    private LocalDate otherDate;

    @ApiModelProperty(value = "总评评价-自述总结")
    private Integer totalSelfSummary;

    @ApiModelProperty(value = "总评评价-答辩过程")
    private Integer totalProcess;

    @ApiModelProperty(value = "总评评价-选题质量")
    private Integer totalQuality;

    @ApiModelProperty(value = "总评评价-完成质量")
    private Integer totalCompleteQuality;

    @ApiModelProperty(value = "总评评价-能力水平")
    private Integer totalAbility;

    @ApiModelProperty(value = "总评评价-评语")
    private String totalComment;

    @ApiModelProperty(value = "总评评价-签署人id")
    private Integer totalPersonId;

    @ApiModelProperty(value = "总评评价-日期")
    private LocalDate totalDate;

    @ApiModelProperty(value = "总分")
    private Integer sumScore;

    @ApiModelProperty(value = "是否二答（1：是二答，0：不是二答）")
    private Integer isSecond;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
