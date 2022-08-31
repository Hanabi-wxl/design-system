package edu.dlu.bysj.document.entity;


/**
 * 过程管理手册
 */

public class ProcessManagementManualTemplate {
    /**
     * 题目名称
     */
    private String subjectTitle;

    /**
     *  学院名称
     */
    private String collegeName;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 学生姓名
     */
    private String studentName;


    /**
     * 指导教师姓名
     */
    private String guideTeacherName;

    /**
     * 指导教师职称
     */
    private String guideTeacherTitle;

    /**
     * 完成时间
     */
    private String completionTime;


    // ------------  任务书部分  -------------

    /**
     * 任务书开始时间
     */
    private String startDate;


    /**
     * 任务书结束时间
     */
    private String endDate;


    /**
     * 选题依据
     */
    private String according;


    /**
     * 论文要求
     */
    private String demand;


    /**
     * 工作重点
     */
    private String emphasis;


    /**
     * 时间安排
     */
    private String schedule;


    /**
     * 目录文献
     */
    private String reference;


    /**
     * 专业负责人
     */
    private String majorLeadingId;


    /**
     * 专业负责人是否同意
     */
    private boolean majorAgree;


    /**
     * 专业负责人签署日期
     */
    private String majorDate;


    /**
     * 学院负责人
     */
    private String collegeLeadingId;


    /**
     * 学院负责人是否同意
     */
    private boolean collegeAgree;


    /**
     * 学院负责人签署日期
     */
    private String collegeDate;


    // ------------  进度检查报表部分  -------------


    //-------------  过程管理评价表部分 --------------

    /**
     * 工作态度
     */
    private Integer processAttitude;


    /**
     * 遵守纪律
     */
    private Integer processDiscipline;


    /**
     * 开题报告
     */
    private Integer processReport;


    /**
     * 任务完成
     */
    private Integer processComplete;


    /**
     * 评语
     */
    private String processComment;


    /**
     * 评价日期
     */
    private String processDate;



    //-------------  指导教师评价表部分  -------------

    /**
     * 选题质量
     */
    private Integer firstQuality;


    /**
     * 能力水平
     */
    private Integer firstAbility;


    /**
     * 完成质量
     */
    private Integer firstComplete;


    /**
     * 指导教师评语
     */
    private String firstComment;


    /**
     * 指导教师评价日期
     */
    private String firstDate;


    //-------------  评阅人评价表部分  --------------

    /**
     * 对应数据库为互评
     */
    /**
     * 选题质量
     */
    private Integer otherQuality;


    /**
     * 能力水平
     */
    private Integer otherAbility;


    /**
     * 完成质量
     */
    private Integer otherComplete;


    /**
     * 评语
     */
    private String otherComment;



    /**
     * 评阅人姓名
     */
    private String otherPersonId;


    /**
     * 评阅日期
     */
    private String otherDate;


    //------------  答辩委员会评价表部分 --------------
    //总评

    /**
     * 自述总结
     */
    private Integer totalSelfSummary;


    /**
     * 答辩过程
     */
    private Integer totalProcess;


    /**
     * 选题质量
     */
    private Integer totalQuality;


    /**
     * 完成质量
     */
    private Integer totalCompleteQuality;


    /**
     * 能力水平
     */
    private Integer totalAbility;


    /**
     * 评语
     */
    private String totalComment;


    /**
     * 总评人
     */
    private String totalPersonId;


    /**
     * 评价日期
     */
    private String totalDate;


}
