package edu.dlu.bysj.base.model.enums;

/**
 * 该系统将整个毕业设计环节分为22个阶段
 *
 * @author XiangXinGang
 * @date 2021/10/10 14:59
 */
public enum ProcessEnum {
    AND_OR_MODIFY_TOPIC_DECLARATION("/paperManagement/approve/teacherCommitSubject", 1),
    DELETE_TITLE("/paperManagement/approve/deleteSubject", 1),
    TOPIC_MAJOR_AUDIT("/paperManagement/approve/submitResult/major", 2),
    TOPIC_COLLEGE_AUDIT("/paperManagement/approve/submitResult/college", 3),
    CHOOSE_TOPIC("/paperManagement/topics/studentSubmit", 4),
    SUBMIT_TASK_BOOK("/paperManagement/taskbook/teacherSubmit", 5),
    TASK_BOOK_MAJOR_AUDIT("/paperManagement/taskbook/submitResult", 6),
    TASK_BOOK_COLLEGE_AUDIT("/paperManagement/taskbook/submitResult", 7),
    SUBMIT_OPEN_REPORT("/fileUpload/openReport", 8),
    OPEN_REPORT_SELF_CHECK("/paperManagement/openReport/submitComment", 9),
    OPEN_REPORT_MAJOR_AUDIT("/paperManagement/openReport/submitResult", 10),
    OPEN_REPORT_COLLEGE_AUDIT("/paperManagement/openReport/submitResult", 11),
    SUBMIT_MIDDLE_CHECK("/paperManagement/middleCheck/content", 12),
    MIDDLE_CHECK_MAJOR_AUDIT("/paperManagement/middleCheck/submitResult ", 13),
    MIDDLE_CHECK_COLLEGE_AUDIT("/paperManagement/middleCheck/submitResult ", 14),
    SUBMIT_PAPER("/paperManagement/fileUpload/paper", 15),
    INSTRUCTOR_EVALUATION("/score/firstTeacher/commentFromScore", 16),
    PROCESS_EVALUATION("/gradeManage/score/process/commentFromScore", 17),
    MUTUAL_EVALUATION("/gradeManage/score/other/comment", 18),
    APPLICATION_DEFENSE("/defenseManagement/defence/application", 19),
    GENERAL_EVALUATION("/gradeManage/score/total/comment", 20),
    TEACHER_SELF_CHECK("/gradeManage/score/selfCheck/content", 21),
    TEACHER_SELF_CHECK_COLLEGE_AUDIT("", 22);


    private String url;
    private Integer processCode;

    ProcessEnum(String url, Integer processCode) {
        this.url = url;
        this.processCode = processCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getProcessCode() {
        return processCode;
    }

    public void setProcessCode(Integer processCode) {
        this.processCode = processCode;
    }

    /**
     * 根据传入的url判断当期接口所在流程编码
     * 当url 不存在是返回null
     *
     * @param url :接口地址
     * @return 流程码或null
     */
    public static Integer backCode(String url) {
        ProcessEnum[] values = ProcessEnum.values();
        for (ProcessEnum element : values) {
            if (element.getUrl().equals(url)) {
                return element.getProcessCode();
            }
        }
        return null;
    }

}
