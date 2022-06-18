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
public class TeacherCommentVo {
    @NotNull(message = "年份不能为空")
    private Integer year;
    @NotBlank(message = "评语不能为空")
    private String comment;
}
