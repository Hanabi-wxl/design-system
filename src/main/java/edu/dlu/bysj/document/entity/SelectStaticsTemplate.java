package edu.dlu.bysj.document.entity;

import lombok.Data;

/**
 * @author sinre
 * @create 06 25, 2022
 * @since 1.0.0
 */
@Data
public class SelectStaticsTemplate {
    /**
     * 题目名称
     */
    private String topicName;

    /**
     * 学生名称
     */
    private String studentName;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 指导教师名称
     */
    private String guideName;

    /**
     * 指导教师职称
     */
    private String guideTitle;

    /**
     * 题目来源
     */
    private String sourceName;

    /**
     * 题目类型
     */
    private String typeName;

    /**
     * 归档需要
     */
    private String fillingNumber;
}
