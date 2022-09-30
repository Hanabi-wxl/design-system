package edu.dlu.bysj.document.entity;

import lombok.Data;

/**
 * @author sinre
 * @create 09 30, 2022
 * @since 1.0.0
 */
@Data
public class GroupMemberTemplate {
    private String serial;
    private String studentNumber;
    private String studentName;
    private String className;
    private String subjectName;
    private String firstTeacherName;
    private String eachName;
}
