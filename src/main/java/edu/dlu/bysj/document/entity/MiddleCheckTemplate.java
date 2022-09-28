package edu.dlu.bysj.document.entity;

import lombok.Data;

/**
 * @author sinre
 * @create 09 28, 2022
 * @since 1.0.0
 */
@Data
public class MiddleCheckTemplate {
    private Integer grade;
    private String subjectName;
    private String teacherName;
    private String teacherTitle;
    private String studentName;
    private Integer literatureQuantity;
    private String finishDate;
    private String workLoad0;
    private String attitude0;
    private String workingProgress0;
    private String workLoad1;
    private String attitude1;
    private String workingProgress1;
    private String workLoad2;
    private String attitude2;
    private String workingProgress2;
    private String hasTaskBook;
    private String hasOpenReport;
    private String conclude;
    private String arrange;
    private String majorAgree;
    private String majorSign;
    private String majorDate;
    private String collegeAgree;
    private String collegeSign;
    private String collegeDate;
}
