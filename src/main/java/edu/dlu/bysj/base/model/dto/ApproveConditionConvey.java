package edu.dlu.bysj.base.model.dto;

import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/10/30 11:32
 */
@Data
public class ApproveConditionConvey {
    private Integer id;
    private String subjectId;
    private String subjectName;

    private String studentName;

    private String studentNumber;

    private String studentId;

    private String studentPhone;

    private String progress;

    private Integer fillingNumber;

    private Integer firstTeacherId;

    private Integer secondTeacherId;
}
