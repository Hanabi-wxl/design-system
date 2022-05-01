package edu.dlu.bysj.base.model.vo.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XiangXinGang
 * @date 2021/11/14 14:39
 */
@Data
public class BasicExcellentVo {
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;
    @ApiModelProperty(value = "成绩分数段(A,B,C,D,F )")
    private String score;
}
