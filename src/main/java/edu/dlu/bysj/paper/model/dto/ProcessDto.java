package edu.dlu.bysj.paper.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sinre
 * @create 05 15, 2022
 * @since 1.0.0
 */
@Data
public class ProcessDto {
    private Integer subjectId;
    private List<Integer> weeks;
}
