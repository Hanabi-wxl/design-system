package edu.dlu.bysj.grade.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author sinre
 * @create 06 18, 2022
 * @since 1.0.0
 */
@Data
public class CommitGoodTeacherVo {
    @NotNull(message = "教师不能为空")
    private Integer teacherId;
    @NotNull(message = "选项不能为空")
    private Integer isGood;
    @NotNull(message = "年份不能为空")
    private Integer year;
}
