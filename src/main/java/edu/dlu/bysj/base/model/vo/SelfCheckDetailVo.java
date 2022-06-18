package edu.dlu.bysj.base.model.vo;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/13 21:57
 */
@Data
public class SelfCheckDetailVo {
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
    @Null
    private Integer taskBook;

    @ApiModelProperty(value = "教师指导")
    @NotNull
    private Integer teacherGuide;

    @ApiModelProperty(value = "同行评阅")
    @Null
    private Integer peerReview;

    @ApiModelProperty(value = "论文答辩")
    @NotNull
    private Integer paperReply;

    @ApiModelProperty(value = "选题")
    @Null
    private Integer chooseSubject;

    @ApiModelProperty(value = "文献应用")
    @NotNull
    private Integer literatureApplication;

    @ApiModelProperty(value = "外文翻译")
    @Null
    private Integer translation;

    @ApiModelProperty(value = "正文")
    @NotNull
    private Integer mainText;

    @ApiModelProperty(value = "自查时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate checkDate;

}
