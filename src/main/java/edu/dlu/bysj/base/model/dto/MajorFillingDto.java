package edu.dlu.bysj.base.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author sinre
 * @create 06 19, 2022
 * @since 1.0.0
 */
@Data
public class MajorFillingDto {
    @NotNull(message = "专业信息不能为空")
    private Integer majorId;
    @NotNull(message = "年份不能为空")
    private Integer year;
}
