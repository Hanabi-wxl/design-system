package edu.dlu.bysj.document.entity;

import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/19 22:03
 */
@Data
public class SubjectApproveFormTemplate {

    // /**
    // * 二维码图片
    // */
    // private ImageEntity qrCode;

    /**
     * 学院名称
     */
    private String college;
    /**
     * 指导教师名称
     */
    private String guideTeacherName;

    /**
     * 指导教师职称
     */
    private String guideTeacherTitle;

    /**
     * 指导教师专业
     */
    private String guideTeacherMajor;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 是否未第一次指导
     */
    private Integer first;

    /**
     * 拟需学生人数
     */

    private Integer needNumber;

    /**
     * 三年内题目是否雷同
     */
    private Integer similar;

    /**
     * 毕业论文类型
     */
    private String paperType;

    /**
     * 选题内容摘要
     */
    private String Content;

    /**
     * 必要性
     */
    private String necessity;

    /**
     * 可行性
     */
    private String feasibility;

    /**
     * 专业审核意见
     */
    private Integer majorOpinion;

    /**
     * 学院审核意见
     */
    private Integer collegeOption;

    /**
     * 指导教师意见-日期(报题日期)
     */
    private String guideOpinionDate;
    /**
     * 审核专业负责人
     */
    private String majorTeacherName;

    /**
     * 审核专业-日期
     */
    private String majorOpinionDate;

    /**
     * 审核院级负责人名称
     */
    private String collegeTeacherName;

    /**
     * 院级审核-日期
     */
    private String collegeOpinionDate;
}
