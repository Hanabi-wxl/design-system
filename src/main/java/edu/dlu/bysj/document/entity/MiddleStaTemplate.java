package edu.dlu.bysj.document.entity;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author sinre
 * @create 10 01, 2022
 * @since 1.0.0
 */
@Data
public class MiddleStaTemplate {
    private String studentName;
    private String subjectName;
    private Integer hasOpenReport;
    private Integer hasTaskBook;
    private Integer literatureQuantity;
    private Integer workingSpeed;
    private LocalDate finishDate;
    private Integer difficulty;
    private Integer collegeAgree;
}
