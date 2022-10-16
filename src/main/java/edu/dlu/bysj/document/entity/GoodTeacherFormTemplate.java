package edu.dlu.bysj.document.entity;

import lombok.Data;

/**
 * @author sinre
 * @create 10 09, 2022
 * @since 1.0.0
 */
@Data
public class GoodTeacherFormTemplate {
    private String teacherName;
    private String collegeName;
    private String majorName;
    private String teacherTitle;
    private Integer guideNumber;
    private Integer workContent;
    private String majorSign;
    private String majorAgree;
    private String majorDate;
    private String collegeSign;
    private String collegeAgree;
    private String collegeDate;
}
