package edu.dlu.bysj.document.entity;

import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/27 20:21
 */
@Data
public class TopicSelectStaticsTemplate {

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
     * 题目来源
     */
    private String topicSource;

    /**
     * 题目类型
     */
    private String topicType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 内容提要
     */
    private String content;

    /**
     * 选题必要性
     */
    private String necessity;

    /**
     * 选题可行性
     */
    private String feasiblity;

}
