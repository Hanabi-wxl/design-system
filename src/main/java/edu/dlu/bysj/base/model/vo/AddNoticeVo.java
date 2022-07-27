package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:49
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "接受新增或修过通知的参数类")
public class AddNoticeVo {

    @ApiModelProperty(value = "学院id")
    private Integer collegeId;

    @ApiModelProperty(value = "专业id")
    @NotNull
    private Integer majorId;

    @ApiModelProperty(value = "通知类型（0：全校通知，1：学院通知，2：专业通知）")
    @Range(min = 0, max = 2)
    private Integer type;

    @ApiModelProperty(value = "通知状态（0：置顶，1：普通，2：隐藏）")
    @Range(min = 0, max = 2)
    private Integer importance;

    @ApiModelProperty(value = "通知标题")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "通知内容")
    @NotBlank
    private String content;

    @ApiModelProperty(value = "文件id")
    private List<Integer> fileId;

    @ApiModelProperty(value = "通知id")
    private Integer noticeId;
}
