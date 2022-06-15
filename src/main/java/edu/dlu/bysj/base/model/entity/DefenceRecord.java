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
 * 答辩记录表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DefenceRecord对象", description="答辩记录表")
public class DefenceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;

    @ApiModelProperty(value = "问题号")
    private Integer questionNumber;

    @ApiModelProperty(value = "问题")
    private String question;

    @ApiModelProperty(value = "回答")
    private String answer;

    @ApiModelProperty(value = "结论，1：正确，0：错误")
    private Integer result;

    @ApiModelProperty(value = "记录日期")
    private LocalDate date;

    @ApiModelProperty(value = "记录人id")
    private Integer noteTakerId;

    // 本数据为保证id唯一不可采用逻辑删除
//    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
//    @TableId
//    private Integer status;
}
