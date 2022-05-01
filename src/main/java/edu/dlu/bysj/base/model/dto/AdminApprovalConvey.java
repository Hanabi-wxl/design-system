package edu.dlu.bysj.base.model.dto;

import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/10/30 9:15
 */
@Data
public class AdminApprovalConvey {
    private Integer subjectId;
    private String subjectName;
    private Integer firstTeacherId;
    private Integer secondTeacherId;
    private Integer studentId;
    private String progress;
    /**
     * 归档序号
     */
    private Integer fillingNumber;
}
