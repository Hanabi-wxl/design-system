package edu.dlu.bysj.document.entity;

import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/17 16:02
 */
@Data
public class PaperCoverTemplate {

    /**
     * 题目名称
     */
    private String subjectTitle;

    /**
     *  学院名称
     */
    private String collegeName;

    /**
     * 年级
     */
    private String grade;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 指导教师姓名
     */
    private String guideTeacherName;
}
