package edu.dlu.bysj.base.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/31 22:30
 */

@Data
@ApiModel(description = "教师查看学生选题志愿返回类")
public class TotalVolunteerPackage<T> {

    @ApiModelProperty(value = "题目id")
    private Integer subjectId;
    @ApiModelProperty(value = "题目名称")
    private String subjectName;

    @ApiModelProperty(value = "第一志愿选题人信息")
    private List<T> firstVolunteer;

    @ApiModelProperty(value = "第二志愿选题人信息")
    private List<T> secondVolunteer;


}
