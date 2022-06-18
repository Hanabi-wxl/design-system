package edu.dlu.bysj.base.model.vo;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/13 21:29
 */
@Data
@ApiModel(description = "自查表参数接收类")
public class SelfCheckVo {
    @ApiModelProperty(value = "题目id")
    @NotNull
    private Integer subjectId;

    @ApiModelProperty(value = "材料归档")
    @NotNull
    private Integer articleArchive;

    @ApiModelProperty(value = "内容填写")
    @NotNull
    private Integer content;

    @ApiModelProperty(value = "研究内容")
    @NotNull
    private Integer researchContent;

    @ApiModelProperty(value = "文献综述")
    @NotNull
    private Integer literatureReview;

    @ApiModelProperty(value = "任务书")
    @NotNull
    private Integer taskBook;

    @ApiModelProperty(value = "教师指导")
    @NotNull
    private Integer teacherGuide;

    @ApiModelProperty(value = "同行评阅")
    @NotNull
    private Integer peerReview;

    @ApiModelProperty(value = "论文答辩")
    @NotNull
    private Integer paperReply;

    @ApiModelProperty(value = "选题")
    @NotNull
    private Integer chooseSubject;

    @ApiModelProperty(value = "文献应用")
    @NotNull
    private Integer literatureApplication;

    @ApiModelProperty(value = "外文翻译")
    @NotNull
    private Integer translation;

    @ApiModelProperty(value = "正文")
    @NotNull
    private Integer mainText;
}
