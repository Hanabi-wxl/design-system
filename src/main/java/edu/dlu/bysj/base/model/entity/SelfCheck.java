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
 * 自查表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SelfCheck对象", description = "自查表")
public class SelfCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "材料归档")
    private Integer articleArchive;

    @ApiModelProperty(value = "内容填写")
    private Integer content;

    @ApiModelProperty(value = "研究内容")
    private Integer reaserchContent;

    @ApiModelProperty(value = "文献综述")
    private Integer literatureReview;

    @ApiModelProperty(value = "任务书")
    private Integer taskbook;

    @ApiModelProperty(value = "教师指导")
    private Integer teacherGuide;

    @ApiModelProperty(value = "同行评阅")
    private Integer peerReview;

    @ApiModelProperty(value = "论文答辩")
    private Integer paperReply;

    @ApiModelProperty(value = "选题")
    private Integer chooseSubject;

    @ApiModelProperty(value = "文献应用")
    private Integer literatureApplication;

    @ApiModelProperty(value = "外文翻译")
    private Integer translation;

    @ApiModelProperty(value = "正文")
    private Integer mainText;

    @ApiModelProperty(value = "此条记录是否有效(1:有效，0：无效)")
    @TableLogic
    private Integer status;

    @ApiModelProperty(value = "自查时间")
    private LocalDate checkDate;

}
