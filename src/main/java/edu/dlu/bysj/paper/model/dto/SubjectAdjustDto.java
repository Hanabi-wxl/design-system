package edu.dlu.bysj.paper.model.dto;

import lombok.Data;

/**
 * @author sinre
 * @create 05 12, 2022
 * @since 1.0.0
 */
@Data
public class SubjectAdjustDto {
    private String subjectId;
    private Integer firstTeacherId;
    private Integer year;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer majorId;
    private String type;
    private String userName;
    private String userNumber;
}
