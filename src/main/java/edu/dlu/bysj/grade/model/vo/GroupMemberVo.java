package edu.dlu.bysj.grade.model.vo;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import lombok.Data;

/**
 * @author sinre
 * @create 06 16, 2022
 * @since 1.0.0
 */
@Data
public class GroupMemberVo{
    private String subjectName;
    private String studentName;
    private String studentNumber;
    private String studentPhone;
    private String process;
    private Integer score;
}
