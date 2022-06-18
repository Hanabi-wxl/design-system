package edu.dlu.bysj.grade.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author sinre
 * @create 06 18, 2022
 * @since 1.0.0
 */
@Data
public class CollegeCommentVo {
    @NotNull(message = "用户不能为空")
    private Integer teacherId;
    @NotNull(message = "年份不能为空")
    private Integer year;
    @NotNull(message = "审核不能为空")
    private Integer agree;
    @NotBlank(message = "评语不能为空")
    private String comment;
}
